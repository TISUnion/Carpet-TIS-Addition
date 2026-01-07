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

package carpettisaddition.utils;

import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class PositionUtils
{
	public static BlockPos flooredBlockPos(Vec3 vec3d)
	{
		//#if MC >= 11904
		//$$ return BlockPos.containing(vec3d);
		//#else
		return new BlockPos(vec3d);
		//#endif
	}

	public static BoundingBox createBlockBox(BlockPos pos1, BlockPos pos2)
	{
		//#if MC >= 11700
		//$$ return BoundingBox.fromCorners(pos1, pos2);
		//#else
		return new BoundingBox(pos1, pos2);
		//#endif
	}

	public static ChunkPos flooredChunkPos(BlockPos pos)
	{
		//#if MC >= 26.1
		//$$ return ChunkPos.containing(pos);
		//#else
		return new ChunkPos(pos);
		//#endif
	}

	public static ChunkPos unpackChunkPos(long data)
	{
		//#if MC >= 26.1
		//$$ return ChunkPos.unpack(data);
		//#else
		return new ChunkPos(data);
		//#endif
	}

	public static int chunkPosX(ChunkPos chunkPos)
	{
		//#if MC >= 26.1
		//$$ return chunkPos.x();
		//#else
		return chunkPos.x;
		//#endif
	}

	public static int chunkPosZ(ChunkPos chunkPos)
	{
		//#if MC >= 26.1
		//$$ return chunkPos.z();
		//#else
		return chunkPos.z;
		//#endif
	}
}
