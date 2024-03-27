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

package carpettisaddition.helpers.rule.natualSpawningUse13Heightmap;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class NatualSpawningUse13HeightmapHelper
{
	public static int sampleHeightmap(World world, Chunk chunk, int x, int z, int initY)
	{
		BlockPos.Mutable pos = new BlockPos.Mutable();

		//#if MC >= 11700
		//$$ final int minY = world.getBottomY();
		//#else
		final int minY = 0;
		//#endif

		int y = initY;
		while (y >= minY)
		{
			pos.set(x, y, z);
			BlockState blockState = chunk.getBlockState(pos);

			boolean ignoredByHeightmap = isSpecialIgnoredBlock(blockState.getBlock()) || isIgnoredByHeightmap(world, pos, blockState);
			if (ignoredByHeightmap)
			{
				y--;
			}
			else
			{
				break;
			}
		}

		return y;
	}

	private static boolean isSpecialIgnoredBlock(Block block)
	{
		if (CarpetTISAdditionSettings.natualSpawningUse13HeightmapExtra)
		{
			return
					block instanceof PistonBlock || block instanceof PistonHeadBlock || block instanceof SlimeBlock
					//#if MC >= 11500
					|| block instanceof net.minecraft.block.HoneyBlock
					//#endif
					;
		}
		return false;
	}

	/**
	 * {@link net.minecraft.world.chunk.light.ChunkLightProvider#getStateForLighting}
	 */
	private static boolean isIgnoredByHeightmap(World world, BlockPos pos, BlockState blockState)
	{
		boolean hasDirectionalOpacity = blockState.isOpaque() && blockState.hasSidedTransparency();
		if (hasDirectionalOpacity)
		{
			VoxelShape voxelUp = blockState.getCullingFace(world, pos, Direction.UP);
			VoxelShape voxelDown = blockState.getCullingFace(world, pos, Direction.DOWN);
			//#if MC >= 11500
			return !VoxelShapes.unionCoversFullCube(voxelUp, voxelDown);
			//#else
			//$$ return !VoxelShapes.method_20713(voxelUp, voxelDown);
			//#endif
		}
		else
		{
			return blockState.getOpacity(world, pos) == 0;
		}
	}

	public static int sampleHeightmap(World world, Chunk chunk, int x, int z)
	{
		return sampleHeightmap(world, chunk, x, z, chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE, x, z));
	}
}
