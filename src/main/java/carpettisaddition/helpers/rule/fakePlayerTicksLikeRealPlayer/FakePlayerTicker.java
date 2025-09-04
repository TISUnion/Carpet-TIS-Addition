/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.fakePlayerTicksLikeRealPlayer;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.utils.PlayerUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.Queue;

public class FakePlayerTicker
{
	private static final FakePlayerTicker INSTANCE = new FakePlayerTicker();

	private final Queue<ActionPackTickTask> pendingActionPackTasks = Queues.newConcurrentLinkedQueue();
	private final List<ServerPlayerEntity> pendingPlayersToBeEntityTicked = Lists.newArrayList();

	public static FakePlayerTicker getInstance()
	{
		return INSTANCE;
	}

	public void addPlayerEntityTick(ServerPlayerEntity player)
	{
		synchronized (this.pendingPlayersToBeEntityTicked)
		{
			this.pendingPlayersToBeEntityTicked.add(player);
		}
	}

	/**
	 * Player action packs are just like regular client packet inputs,
	 * so tick them in the async-task phase
	 */
	public void addActionPackTick(ServerPlayerEntity player, EntityPlayerActionPack actionPack)
	{
		MinecraftServer server = PlayerUtils.getServerFromPlayer(player);
		if (server != null)
		{
			Runnable runnable = transformActionPackTickTask(actionPack::onUpdate);
			ActionPackTickTask task = new ActionPackTickTask(player, runnable);
			this.pendingActionPackTasks.add(task);
		}
	}

	private static Runnable transformActionPackTickTask(Runnable task)
	{
		// microtiming do your mixin here
		return task;
	}

	/**
	 * References:
	 * - {@link net.minecraft.server.network.ServerPlayNetworkHandler#tick} for where {@link ServerPlayerEntity#playerTick} is invoked
	 * - {@link net.minecraft.server.ServerNetworkIo#tick} for exception handling
	 */
	public void networkPhaseTick()
	{
		List<ServerPlayerEntity> players = Lists.newArrayList();
		synchronized (this.pendingPlayersToBeEntityTicked)
		{
			players.addAll(this.pendingPlayersToBeEntityTicked);
			this.pendingPlayersToBeEntityTicked.clear();
		}

		for (ServerPlayerEntity player : players)
		{
			try
			{
				//#if MC >= 11500
				player.playerTick();
				//#else
				//$$ player.method_14226();
				//#endif
			}
			catch (Exception e)
			{
				CarpetTISAdditionMod.LOGGER.warn("Failed to perform entity tick for carpet fake player {}", player.getName(), e);
			}
		}
	}

	/**
	 * The implementation of submitting task to the MinecraftServer task executor does not work correctly,
	 * since tasks submitted there are not guaranteed to be executed at the next async task phase,
	 * especially when tick warping
	 * <p>
	 * See also: {@link net.minecraft.server.MinecraftServer#canExecute}. There may be up to a 4-tick delay
	 */
	public void asyncTaskPhaseTick()
	{
		while (true)
		{
			ActionPackTickTask task = this.pendingActionPackTasks.poll();
			if (task == null)
			{
				break;
			}

			ServerPlayerEntity player = task.player;
			Runnable runnable = task.runnable;
			try
			{
				runnable.run();
			}
			catch (Exception e)
			{
				CarpetTISAdditionMod.LOGGER.error("Failed to perform player action pack tick for player {}", player.getName(), e);
			}
		}
	}

	private static class ActionPackTickTask
	{
		public final ServerPlayerEntity player;
		public final Runnable runnable;

		public ActionPackTickTask(ServerPlayerEntity player, Runnable runnable)
		{
			this.player = player;
			this.runnable = runnable;
		}
	}
}
