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
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.CompressionEncoder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.arguments.EntityArgument.getPlayers;
import static net.minecraft.commands.arguments.EntityArgument.players;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

//#if MC >= 11901
//$$ import net.minecraft.network.PacketSendListener;
//#endif

//#if MC >= 11800
//$$ import net.minecraft.core.SectionPos;
//#endif

//#if MC >= 11600 && MC < 11900
//$$ import net.minecraft.Util;
//#endif

//#if MC >= 11901
//$$ import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
//#else
import net.minecraft.network.protocol.game.ClientboundChatPacket;
//#endif

public class RefreshCommand extends AbstractCommand
{
	private static final String NAME = "refresh";
	private static final RefreshCommand INSTANCE = new RefreshCommand();

	/**
	 * `/refresh chunk all` is costly on network thread due to packet compression in {@link CompressionEncoder}
	 * Here's a check to see if the connection thread is already heavily-loaded
	 */
	private final Set<ServerPlayer> refreshingChunkPlayers = Collections.newSetFromMap(new WeakHashMap<>());

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
		LiteralArgumentBuilder<CommandSourceStack> builder = literal(NAME).
				requires((player) -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandRefresh)).
				then(
						literal("inventory").
						executes(c -> refreshSelfInventory(c.getSource())).
						then(
								argument("players", players()).
								requires(s -> CommandUtils.hasPermissionLevel(s, 2)).
								executes(c -> refreshSelectedPlayerInventory(c.getSource(), getPlayers(c, "players")))
						)
				).
				then(
						literal("chunk").
						executes(c -> refreshCurrentChunk(c.getSource(), c.getSource().getPlayerOrException())).
						then(literal("current").executes(c -> refreshCurrentChunk(c.getSource(), c.getSource().getPlayerOrException()))).
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

	private void refreshPlayerInventory(CommandSourceStack source, ServerPlayer player)
	{
		source.getServer().getPlayerList().sendAllPlayerInfo(player);
		Messenger.tell(player, tr("inventory.done"));
	}

	private int refreshSelfInventory(CommandSourceStack source) throws CommandSyntaxException
	{
		this.refreshPlayerInventory(source, source.getPlayerOrException());
		return 1;
	}

	private int refreshSelectedPlayerInventory(CommandSourceStack source, Collection<ServerPlayer> players)
	{
		players.forEach(player -> this.refreshPlayerInventory(source, player));
		return players.size();
	}

	/*
	 * ---------------------
	 *     Refresh Chunk
	 * ---------------------
	 */

	private int refreshChunks(CommandSourceStack source, @Nullable ChunkPos chunkPos, @Nullable Predicate<ChunkPos> predicate) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		ServerLevel world = player.getLevel();
		synchronized (this.refreshingChunkPlayers)
		{
			if (this.refreshingChunkPlayers.contains(player))
			{
				Messenger.tell(source, Messenger.formatting(tr("chunk.overloaded"), "r"));
				return 0;
			}
		}
		ThreadedAnvilChunkStorageAccessor chunkStorage = (ThreadedAnvilChunkStorageAccessor)world.getChunkSource().chunkMap;
		MutableInt counter = new MutableInt(0);

		Consumer<LevelChunk> chunkRefresher = (chunk) -> {
			if (chunk != null)
			{
				new ChunkRefresher(chunk).refreshFor(player);
				counter.add(1);
			}
		};

		Predicate<ChunkPos> inPlayerViewDistance = pos -> isChunkInsideRange(pos, player, chunkStorage.getWatchDistance());
		if (chunkPos != null)
		{
			if (inPlayerViewDistance.test(chunkPos))
			{
				chunkRefresher.accept(world.getChunk(chunkPos.x, chunkPos.z));
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
					forEach(h -> chunkRefresher.accept(h.getTickingChunk()));
			synchronized (this.refreshingChunkPlayers)
			{
				this.refreshingChunkPlayers.add(player);
			}
		}
		BaseComponent message = TISAdditionTranslations.translate(tr("chunk.done", counter.intValue()), player);

		//#if MC >= 12002
		//$$ player.networkHandler.send(
		//#else
		player.connection.send(
		//#endif
				//#if MC >= 11901
				//$$ new ClientboundSystemChatPacket(message, false),
				//#elseif MC >= 11600
				//$$ new ClientboundChatPacket(message, ChatType.SYSTEM, Util.NIL_UUID),
				//#else
				new ClientboundChatPacket(message, ChatType.SYSTEM),
				//#endif

				//#if MC >= 11901
				//$$ PacketSendListener.thenRun(() -> {
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
		);
		return counter.intValue();
	}
	private int refreshSingleChunk(CommandSourceStack source, @Nullable ChunkPos chunkPos) throws CommandSyntaxException
	{
		return this.refreshChunks(source, chunkPos, null);
	}

	private int refreshAllChunks(CommandSourceStack source) throws CommandSyntaxException
	{
		return this.refreshChunks(source, null, chunkPos -> true);
	}

	private int refreshCurrentChunk(CommandSourceStack source, ServerPlayer player) throws CommandSyntaxException
	{
		return this.refreshSingleChunk(source, new ChunkPos(
				//#if MC >= 1.16.0
				//$$ player.blockPosition()
				//#else
				player.getCommandSenderBlockPosition()
				//#endif
		));
	}

	private int refreshSelectedChunk(CommandSourceStack source, int x, int z) throws CommandSyntaxException
	{
		return this.refreshSingleChunk(source, new ChunkPos(x, z));
	}

	private int refreshChunksInRange(CommandSourceStack source, int distance) throws CommandSyntaxException
	{
		ServerPlayer player = source.getPlayerOrException();
		return this.refreshChunks(source, null, chunkPos -> isChunkInsideRange(chunkPos, player, distance));
	}

	private static boolean isChunkInsideRange(ChunkPos chunkPos, ServerPlayer player, int distance)
	{
		//#if MC >= 11800
		//$$ SectionPos watchedSection = player.getLastSectionPos();
		//$$ return EuclideanDistanceHelper.isWithinDistance(chunkPos.x, chunkPos.z, watchedSection.x(), watchedSection.z(), distance);
		//#else
		return ThreadedAnvilChunkStorageAccessor.invokeGetChebyshevDistance(chunkPos, player, true) <= distance;
		//#endif
	}
}
