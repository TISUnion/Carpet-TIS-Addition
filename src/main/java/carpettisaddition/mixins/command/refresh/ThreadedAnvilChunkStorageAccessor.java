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

package carpettisaddition.mixins.command.refresh;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

//#if MC >= 11800
//$$ import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
//$$ import org.apache.commons.lang3.mutable.MutableObject;
//#else
import net.minecraft.network.Packet;
//#endif

@Mixin(ThreadedAnvilChunkStorage.class)
public interface ThreadedAnvilChunkStorageAccessor
{
	@Accessor
	Long2ObjectLinkedOpenHashMap<ChunkHolder> getCurrentChunkHolders();

	@Accessor
	int getWatchDistance();

	//#if MC >= 11800
	//$$ // isChunkWithinEuclideanDistanceRange or whatever
	//$$ @Invoker("isWithinDistance")
	//$$ static boolean invokeIsChunkWithinEuclideanDistanceRange(int chunkX, int chunkZ, int playerSectionX, int playerSectionZ, int distance)
	//$$ {
	//$$ 	return false;
	//$$ }
	//#endif

	//#if MC < 11800
	@Invoker
	static int invokeGetChebyshevDistance(ChunkPos pos, ServerPlayerEntity player, boolean useCameraPosition)
	{
		return 0;
	}
	//#endif

	@Invoker
	void invokeSendWatchPackets(
			ServerPlayerEntity player, ChunkPos pos,
			//#if MC >= 11800
			//$$ MutableObject<ChunkDataS2CPacket> mutableObject,
			//#else
			Packet<?>[] packets,
			//#endif
			boolean withinMaxWatchDistance, boolean withinViewDistance
	);
}
