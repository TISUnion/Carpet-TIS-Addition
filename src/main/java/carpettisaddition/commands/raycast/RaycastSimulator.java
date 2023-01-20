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

package carpettisaddition.commands.raycast;

import com.google.common.collect.Lists;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.function.Supplier;

public class RaycastSimulator
{
	private static final BlockState AIR = Blocks.AIR.getDefaultState();
	private static final BlockState FLOOR = Blocks.SMOOTH_STONE.getDefaultState();
	private static final BlockState FLOOR2 = Blocks.WHITE_CONCRETE.getDefaultState();
	private static final BlockState MELON = Blocks.MELON.getDefaultState();
	private static final BlockState CENTER = Blocks.STONE_BUTTON.getDefaultState().with(AbstractButtonBlock.FACE, WallMountLocation.FLOOR);
	private static final BlockState BARRIER = Blocks.POLISHED_GRANITE.getDefaultState();
	private static final BlockState PARTIAL = Blocks.RED_CARPET.getDefaultState();
	private final World world;
	private final Entity entity;

	public RaycastSimulator(World world, Entity entity)
	{
		this.world = world;
		this.entity = entity;
	}

	private boolean canTraceMelon(BlockPos start, BlockPos end)
	{
		BlockHitResult result = this.world.rayTrace(new RayTraceContext(
				new Vec3d(start.getX() + 0.5D, start.getY() + 0.5D, start.getZ() + 0.5D),
				new Vec3d(end.getX() + 0.5D, end.getY() + 0.5D, end.getZ() + 0.5D),
				RayTraceContext.ShapeType.OUTLINE,
				RayTraceContext.FluidHandling.NONE,
				this.entity
		));
		if (result.getType() == HitResult.Type.BLOCK)
		{
			return this.world.getBlockState(result.getBlockPos()).getBlock() == MELON.getBlock();
		}
		return false;
	}

	private void simulate1Layer(BlockPos centerPos, int r)
	{
		this.world.setBlockState(centerPos, CENTER);
		for (BlockPos pos : BlockPos.iterate(centerPos.add(-r, -1, -r), centerPos.add(r, -1, r)))
		{
			this.world.setBlockState(pos, FLOOR);
		}
		for (BlockPos pos : BlockPos.iterate(centerPos.add(-r, 0, -r), centerPos.add(r, 0, r)))
		{
			this.world.setBlockState(pos, AIR);
		}

		ArrayList<BlockPos> melonPos = Lists.newArrayList(
				centerPos.add(-r, 0, -r),
				centerPos.add(-r, 0, +r),
				centerPos.add(+r, 0, -r),
				centerPos.add(+r, 0, +r)
		);
		melonPos.forEach(pos -> this.world.setBlockState(pos, MELON));
		Supplier<Long> counter = () -> melonPos.stream().filter(pos -> this.canTraceMelon(centerPos, pos)).count();

		for (BlockPos pos : BlockPos.iterate(centerPos.add(-r, 0, -r), centerPos.add(r, 0, r)))
		{
			if (this.world.getBlockState(pos).isAir())
			{
				this.world.setBlockState(pos, BARRIER);
				Long count = counter.get();
				if (count < melonPos.size())
				{
					this.world.setBlockState(pos, AIR);
				}
			}
		}
	}

	public void simulate(BlockPos startPos, int maxR)
	{
		BlockPos pos = startPos;
		for (int r = 1; r <= maxR; r++)
		{
			this.simulate1Layer(pos, r);
			if (r < maxR)
			{
				pos = pos.add(0, 0, r * 2 + 4);
			}
		}

		for (BlockPos p : BlockPos.iterate(startPos.add(-maxR - 1, -1, -2), pos.add(maxR + 1, -1, maxR + 1)))
		{
			if (this.world.getBlockState(p).isAir())
			{
				this.world.setBlockState(p, FLOOR2);
			}
		}
	}
}
