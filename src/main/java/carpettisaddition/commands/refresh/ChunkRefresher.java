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

package carpettisaddition.commands.refresh;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.mixins.command.refresh.ThreadedAnvilChunkStorageAccessor;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.world.chunk.WorldChunk;

//#if MC >= 12002
//$$ import carpettisaddition.mixins.command.refresh.ChunkDataSenderAccessor;
//#endif

//#if MC >= 11800
//$$ import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
//$$ import net.minecraft.util.math.ChunkSectionPos;
//$$ import org.apache.commons.lang3.mutable.MutableObject;
//#endif

public class ChunkRefresher
{
	private final WorldChunk chunk;

	//#if MC >= 11800
	//$$ private final MutableObject<ChunkDataS2CPacket> packetCache = new MutableObject<>();
	//#else
	private final Packet<?>[] packetCache = new Packet[2];
	//#endif

	public ChunkRefresher(WorldChunk chunk)
	{
		this.chunk = chunk;
	}

	public void refreshForAllNearby(ServerWorld world)
	{
		if (world != this.chunk.getWorld())
		{
			CarpetTISAdditionMod.LOGGER.warn("ChunkRefresher world mismatch, given {}, chunk in {}", world, this.chunk.getWorld());
			return;
		}
		ThreadedAnvilChunkStorage chunkStorage = world.getChunkManager().threadedAnvilChunkStorage;
		chunkStorage.getPlayersWatchingChunk(this.chunk.getPos(), false).
				forEach(this::refreshFor);
	}

	public void refreshFor(ServerPlayerEntity player)
	{
		ServerWorld world = player.getServerWorld();
		if (world != this.chunk.getWorld())
		{
			CarpetTISAdditionMod.LOGGER.warn("ChunkRefresher world mismatch, player in {}, chunk in {}", world, this.chunk.getWorld());
			return;
		}

		//#if MC >= 12002
		//$$ ChunkDataSenderAccessor.invokeSendChunkPacket(player.networkHandler, world, chunk);
		//#else
		((ThreadedAnvilChunkStorageAccessor)world.getChunkManager().threadedAnvilChunkStorage).invokeSendWatchPackets(
				player, this.chunk.getPos(),
				this.packetCache,
				false, true
		);
		//#endif
	}
}
