/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.commands.refresh;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.mixins.command.refresh.ThreadedAnvilChunkStorageAccessor;
import carpettisaddition.translations.TISAdditionTranslations;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.MessageType;
import net.minecraft.network.PacketDeflater;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.command.arguments.EntityArgumentType.getPlayers;
import static net.minecraft.command.arguments.EntityArgumentType.players;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

//#if MC >= 12002
//$$ import carpettisaddition.mixins.command.refresh.ChunkDataSenderAccessor;
//#endif

//#if MC >= 11901
//$$ import net.minecraft.network.PacketCallbacks;
//#endif

//#if MC >= 11800
//$$ import net.minecraft.util.math.ChunkSectionPos;
//$$ import org.apache.commons.lang3.mutable.MutableObject;
//#else
import net.minecraft.network.Packet;
//#endif

//#if MC >= 11600
//$$ import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
//#if MC < 11900
//$$ import net.minecraft.util.Util;
//#endif
//#else
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
//#endif

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
	public void registerCommand(CommandTreeContext.Register context)
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
		context.dispatcher.register(builder);
	}

	/*
	 * ---------------------
	 *   Refresh Inventory
	 * ---------------------
	 */

	private void refreshPlayerInventory(ServerCommandSource source, ServerPlayerEntity player)
	{
		source.getMinecraftServer().getPlayerManager().method_14594(player);
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
		ServerWorld world = player.getServerWorld();
		synchronized (this.refreshingChunkPlayers)
		{
			if (this.refreshingChunkPlayers.contains(player))
			{
				Messenger.tell(source, Messenger.formatting(tr("chunk.overloaded"), "r"));
				return 0;
			}
		}
		ThreadedAnvilChunkStorageAccessor chunkStorage = (ThreadedAnvilChunkStorageAccessor)world.getChunkManager().threadedAnvilChunkStorage;
		MutableInt counter = new MutableInt(0);

		// in mc < 1.20.2, only the "pos" arg is used
		// in mc >= 1.20.2, only the "chunk" arg is used
		BiConsumer<WorldChunk, ChunkPos> chunkRefresher = (chunk, pos) -> {
			//#if MC >= 12002
			//$$ ChunkDataSenderAccessor.invokeSendChunkPacket(player.networkHandler, world, chunk);
			//#else
			chunkStorage.invokeSendWatchPackets(
					player, pos,
					//#if MC >= 11800
					//$$ new MutableObject<>(),
					//#else
					new Packet[2],
					//#endif
					false, true
			);
			//#endif
			counter.add(1);
		};

		Predicate<ChunkPos> inPlayerViewDistance = pos -> isChunkInsideRange(pos, player, chunkStorage.getWatchDistance());
		if (chunkPos != null)
		{
			if (inPlayerViewDistance.test(chunkPos))
			{
				chunkRefresher.accept(
						//#if MC >= 12002
						//$$ world.getChunk(chunkPos.x, chunkPos.z),
						//#else
						null,
						//#endif
						chunkPos
				);
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
					filter(h -> inPlayerViewDistance.and(predicate).test(h.getPos())).
					forEach(h -> chunkRefresher.accept(h.getWorldChunk(), h.getPos()));
			synchronized (this.refreshingChunkPlayers)
			{
				this.refreshingChunkPlayers.add(player);
			}
		}
		BaseText message = TISAdditionTranslations.translate(tr("chunk.done", counter.getValue()), player);

		//#if MC >= 12002
		//$$ player.networkHandler.send(
		//#else
		player.networkHandler.sendPacket(
		//#endif
				//#if MC >= 11901
				//$$ new GameMessageS2CPacket(message, false),
				//#elseif MC >= 11600
				//$$ new GameMessageS2CPacket(message, MessageType.SYSTEM, Util.NIL_UUID),
				//#else
				new ChatMessageS2CPacket(message, MessageType.SYSTEM),
				//#endif

				//#if MC >= 11901
				//$$ PacketCallbacks.always(() -> {
				//#else
				future -> {
				//#endif
					synchronized (this.refreshingChunkPlayers)
					{
						this.refreshingChunkPlayers.remove(player);
					}
				//#if MC >= 11901
				//$$ })
				//#else
				}
				//#endif

				//#if MC >= 12002
				//$$ , true
				//#endif
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
		return this.refreshChunks(source, null, chunkPos -> isChunkInsideRange(chunkPos, player, distance));
	}

	private static boolean isChunkInsideRange(ChunkPos chunkPos, ServerPlayerEntity player, int distance)
	{
		//#if MC >= 11800
		//$$ ChunkSectionPos watchedSection = player.getWatchedSection();
		//$$ return EuclideanDistanceHelper.isWithinDistance(chunkPos.x, chunkPos.z, watchedSection.getSectionX(), watchedSection.getSectionZ(), distance);
		//#else
		return ThreadedAnvilChunkStorageAccessor.invokeGetChebyshevDistance(chunkPos, player, true) <= distance;
		//#endif
	}
}
