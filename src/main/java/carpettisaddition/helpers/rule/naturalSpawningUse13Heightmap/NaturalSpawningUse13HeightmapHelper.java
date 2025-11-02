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

package carpettisaddition.helpers.rule.naturalSpawningUse13Heightmap;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

public class NaturalSpawningUse13HeightmapHelper
{
	public static int sampleHeightmap(Level world, ChunkAccess chunk, int x, int z, int initY)
	{
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

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
		if (CarpetTISAdditionSettings.naturalSpawningUse13HeightmapExtra)
		{
			return
					block instanceof PistonBaseBlock || block instanceof PistonHeadBlock || block instanceof SlimeBlock
					//#if MC >= 11500
					|| block instanceof net.minecraft.world.level.block.HoneyBlock
					//#endif
					;
		}
		return false;
	}

	/**
	 * {@link net.minecraft.world.level.lighting.LayerLightEngine#getStateForLighting}
	 */
	private static boolean isIgnoredByHeightmap(Level world, BlockPos pos, BlockState blockState)
	{
		boolean hasDirectionalOpacity = blockState.canOcclude() && blockState.useShapeForLightOcclusion();
		if (hasDirectionalOpacity)
		{
			VoxelShape voxelUp = blockState.getFaceOcclusionShape(
					//#if MC < 12102
					world, pos,
					//#endif
					Direction.UP
			);
			VoxelShape voxelDown = blockState.getFaceOcclusionShape(
					//#if MC < 12102
					world, pos,
					//#endif
					Direction.DOWN
			);
			return !Shapes.faceShapeOccludes(voxelUp, voxelDown);
		}
		else
		{
			return blockState.getLightBlock(
					//#if MC < 12102
					world, pos
					//#endif
			) == 0;
		}
	}

	public static int sampleHeightmap(Level world, ChunkAccess chunk, int x, int z)
	{
		return sampleHeightmap(world, chunk, x, z, chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z));
	}
}
