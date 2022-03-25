package carpettisaddition.commands.refresh;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.mixins.command.refresh.ThreadedAnvilChunkStorageAccessor;
import carpettisaddition.translations.TISAdditionTranslations;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.network.MessageType;
import net.minecraft.network.PacketDeflater;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.text.BaseText;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.command.argument.EntityArgumentType.getPlayers;
import static net.minecraft.command.argument.EntityArgumentType.players;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RefreshCommand extends AbstractCommand
{
	private static final String NAME = "refresh";
	private static final RefreshCommand INSTANCE = new RefreshCommand();

	/**
	 * `/refresh chunk all` is costly on network thread due to packet compression in {@link PacketDeflater}
	 * Here's a check to see if the connection thread is already heavily-loaded
	 */
	private final Set<ServerPlayerEntity> refreshingChunkPlayers = Collections.newSetFromMap(new WeakHashMap<>());

	public RefreshCommand()
	{
		super(NAME);
	}

	public static RefreshCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandBuildContext)
	{
		LiteralArgumentBuilder<ServerCommandSource> builder = literal(NAME).
				requires((player) -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandRefresh)).
				then(
						literal("inventory").
						executes(c -> refreshSelfInventory(c.getSource())).
						then(
								argument("players", players()).
								requires(s -> s.hasPermissionLevel(2)).
								executes(c -> refreshSelectedPlayerInventory(c.getSource(), getPlayers(c, "players")))
						)
				).
				then(
						literal("chunk").
						executes(c -> refreshCurrentChunk(c.getSource(), c.getSource().getPlayer())).
						then(literal("current").executes(c -> refreshCurrentChunk(c.getSource(), c.getSource().getPlayer()))).
						then(literal("all").executes(c -> refreshAllChunks(c.getSource()))).
						then(literal("inrange").then(
								argument("chebyshevDistance", integer()).
								executes(c -> refreshChunksInRange(c.getSource(), getInteger(c, "chebyshevDistance")))
						)).
						then(literal("at").
								then(argument("chunkX", integer()).
										then(
												argument("chunkZ", integer()).
												executes(c -> refreshSelectedChunk(c.getSource(), getInteger(c, "chunkX"), getInteger(c, "chunkZ")))
										)
								)
						)
				);
		dispatcher.register(builder);
	}

	/*
	 * ---------------------
	 *   Refresh Inventory
	 * ---------------------
	 */

	private void refreshPlayerInventory(ServerCommandSource source, ServerPlayerEntity player)
	{
		source.getServer().getPlayerManager().sendPlayerStatus(player);
		Messenger.tell(player, tr("inventory.done"));
	}

	private int refreshSelfInventory(ServerCommandSource source) throws CommandSyntaxException
	{
		this.refreshPlayerInventory(source, source.getPlayer());
		return 1;
	}

	private int refreshSelectedPlayerInventory(ServerCommandSource source, Collection<ServerPlayerEntity> players)
	{
		players.forEach(player -> this.refreshPlayerInventory(source, player));
		return players.size();
	}

	/*
	 * ---------------------
	 *     Refresh Chunk
	 * ---------------------
	 */

	private int refreshChunks(ServerCommandSource source, @Nullable ChunkPos chunkPos, @Nullable Predicate<ChunkPos> predicate) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayer();
		synchronized (this.refreshingChunkPlayers)
		{
			if (this.refreshingChunkPlayers.contains(player))
			{
				Messenger.tell(source, Messenger.formatting(tr("chunk.overloaded"), "r"));
				return 0;
			}
		}
		ThreadedAnvilChunkStorageAccessor chunkStorage = (ThreadedAnvilChunkStorageAccessor)source.getPlayer().getWorld().getChunkManager().threadedAnvilChunkStorage;
		MutableInt counter = new MutableInt(0);
		Consumer<ChunkPos> chunkRefresher = pos -> {
			chunkStorage.invokeSendWatchPackets(player, pos, new MutableObject<>(), false, true);
			counter.add(1);
		};
		Predicate<ChunkPos> inPlayerViewDistance = pos -> isChunkWithinEuclideanDistanceRange(pos, player, chunkStorage.getWatchDistance());
		if (chunkPos != null)
		{
			if (inPlayerViewDistance.test(chunkPos))
			{
				chunkRefresher.accept(chunkPos);
			}
			else
			{
				Messenger.tell(source, Messenger.formatting(tr("chunk.too_far"), "r"));
			}
		}
		else
		{
			Objects.requireNonNull(predicate);
			chunkStorage.getCurrentChunkHolders().values().stream().
					map(ChunkHolder::getPos).
					filter(inPlayerViewDistance.and(predicate)).
					forEach(chunkRefresher);
			synchronized (this.refreshingChunkPlayers)
			{
				this.refreshingChunkPlayers.add(player);
			}
		}
		BaseText message = TISAdditionTranslations.translate(tr("chunk.done", counter.getValue()), player);
		player.networkHandler.sendPacket(
				new GameMessageS2CPacket(message, MessageType.SYSTEM, Util.NIL_UUID),
				future -> {
					synchronized (this.refreshingChunkPlayers)
					{
						this.refreshingChunkPlayers.remove(player);
					}
				}
		);
		return counter.getValue();
	}
	private int refreshSingleChunk(ServerCommandSource source, @Nullable ChunkPos chunkPos) throws CommandSyntaxException
	{
		return this.refreshChunks(source, chunkPos, null);
	}

	private int refreshAllChunks(ServerCommandSource source) throws CommandSyntaxException
	{
		return this.refreshChunks(source, null, chunkPos -> true);
	}

	private int refreshCurrentChunk(ServerCommandSource source, ServerPlayerEntity player) throws CommandSyntaxException
	{
		return this.refreshSingleChunk(source, new ChunkPos(player.getBlockPos()));
	}

	private int refreshSelectedChunk(ServerCommandSource source, int x, int z) throws CommandSyntaxException
	{
		return this.refreshSingleChunk(source, new ChunkPos(x, z));
	}

	private int refreshChunksInRange(ServerCommandSource source, int distance) throws CommandSyntaxException
	{
		ServerPlayerEntity player = source.getPlayer();
		return this.refreshChunks(source, null, chunkPos -> isChunkWithinEuclideanDistanceRange(chunkPos, player, distance));
	}

	private static boolean isChunkWithinEuclideanDistanceRange(ChunkPos chunkPos, ServerPlayerEntity player, int distance)
	{
		ChunkSectionPos watchedSection = player.getWatchedSection();
		return ThreadedAnvilChunkStorageAccessor.invokeIsChunkWithinEuclideanDistanceRange(chunkPos.x, chunkPos.z, watchedSection.getSectionX(), watchedSection.getSectionZ(), distance);
	}
}
