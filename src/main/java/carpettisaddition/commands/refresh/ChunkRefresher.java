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
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.LevelChunk;

//#if MC >= 12002
//$$ import carpettisaddition.mixins.command.refresh.ChunkDataSenderAccessor;
//#endif

//#if MC >= 11800
//$$ import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
//$$ import net.minecraft.core.SectionPos;
//$$ import org.apache.commons.lang3.mutable.MutableObject;
//#endif

public class ChunkRefresher
{
	private final LevelChunk chunk;

	//#if MC >= 11800
	//$$ private final MutableObject<ClientboundLevelChunkWithLightPacket> packetCache = new MutableObject<>();
	//#else
	private final Packet<?>[] packetCache = new Packet[2];
	//#endif

	public ChunkRefresher(LevelChunk chunk)
	{
		this.chunk = chunk;
	}

	public void refreshForAllNearby(ServerLevel world)
	{
		if (world != this.chunk.getLevel())
		{
			CarpetTISAdditionMod.LOGGER.warn("ChunkRefresher world mismatch, given {}, chunk in {}", world, this.chunk.getLevel());
			return;
		}
		ChunkMap chunkStorage = world.getChunkSource().chunkMap;
		chunkStorage.getPlayers(this.chunk.getPos(), false).
				forEach(this::refreshFor);
	}

	public void refreshFor(ServerPlayer player)
	{
		ServerLevel world = player.getLevel();
		if (world != this.chunk.getLevel())
		{
			CarpetTISAdditionMod.LOGGER.warn("ChunkRefresher world mismatch, player in {}, chunk in {}", world, this.chunk.getLevel());
			return;
		}

		//#if MC >= 12002
		//$$ ChunkDataSenderAccessor.invokeSendChunkPacket(player.networkHandler, world, chunk);
		//#else
		((ThreadedAnvilChunkStorageAccessor)world.getChunkSource().chunkMap).invokeSendWatchPackets(
				player, this.chunk.getPos(),
				this.packetCache,
				false, true
		);
		//#endif
	}
}
