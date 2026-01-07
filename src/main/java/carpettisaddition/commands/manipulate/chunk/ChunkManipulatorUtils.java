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

package carpettisaddition.commands.manipulate.chunk;

import carpettisaddition.commands.refresh.ChunkRefresher;
import carpettisaddition.mixins.command.manipulate.chunk.ServerLightingProviderAccessor;
import carpettisaddition.utils.PositionUtils;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.concurrent.CompletableFuture;

public class ChunkManipulatorUtils
{
	public static void refreshChunk(ServerLevel world, LevelChunk chunk)
	{
		new ChunkRefresher(chunk).refreshForAllNearby(world);
	}

	public static void refreshChunkLight(ServerLevel world, LevelChunk chunk)
	{
		refreshChunkLight(world, chunk.getPos());
	}

	public static void refreshChunkLight(ServerLevel world, ChunkPos chunkPos)
	{
		ClientboundLightUpdatePacket packet = new ClientboundLightUpdatePacket(
				chunkPos, world.getChunkSource().getLightEngine()
				//#if MC >= 11700
				//$$ , null, null
				//#endif
				//#if 11600 <= MC && MC < 12000
				//$$ , true
				//#endif
		);
		ChunkMap chunkStorage = world.getChunkSource().chunkMap;
		chunkStorage.getPlayers(chunkPos, false).
				forEach(player -> player.connection.send(packet));
	}

	public static CompletableFuture<Void> enqueueDummyLightingTask(ThreadedLevelLightEngine lightingProvider, ChunkPos chunkPos)
	{
		CompletableFuture<Void> future = new CompletableFuture<>();
		((ServerLightingProviderAccessor)lightingProvider).invokeEnqueue(
				PositionUtils.chunkPosX(chunkPos), PositionUtils.chunkPosZ(chunkPos),
				ThreadedLevelLightEngine.TaskType.POST_UPDATE,
				() -> future.complete(null)
		);
		return future;
	}
}
