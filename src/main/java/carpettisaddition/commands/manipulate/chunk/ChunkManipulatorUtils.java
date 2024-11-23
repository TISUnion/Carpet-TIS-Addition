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
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.concurrent.CompletableFuture;

public class ChunkManipulatorUtils
{
	public static void refreshChunk(ServerWorld world, WorldChunk chunk)
	{
		new ChunkRefresher(chunk).refreshForAllNearby(world);
	}

	public static void refreshChunkLight(ServerWorld world, WorldChunk chunk)
	{
		refreshChunkLight(world, chunk.getPos());
	}

	public static void refreshChunkLight(ServerWorld world, ChunkPos chunkPos)
	{
		LightUpdateS2CPacket packet = new LightUpdateS2CPacket(
				chunkPos, world.getChunkManager().getLightingProvider()
				//#if MC >= 11700
				//$$ , null, null
				//#endif
				//#if 11600 <= MC && MC < 12000
				//$$ , true
				//#endif
		);
		ThreadedAnvilChunkStorage chunkStorage = world.getChunkManager().threadedAnvilChunkStorage;
		chunkStorage.getPlayersWatchingChunk(chunkPos, false).
				forEach(player -> player.networkHandler.sendPacket(packet));
	}

	public static CompletableFuture<Void> enqueueDummyLightingTask(ServerLightingProvider lightingProvider, ChunkPos chunkPos)
	{
		CompletableFuture<Void> future = new CompletableFuture<>();
		((ServerLightingProviderAccessor)lightingProvider).invokeEnqueue(
				chunkPos.x, chunkPos.z, ServerLightingProvider.Stage.POST_UPDATE,
				() -> future.complete(null)
		);
		return future;
	}
}
