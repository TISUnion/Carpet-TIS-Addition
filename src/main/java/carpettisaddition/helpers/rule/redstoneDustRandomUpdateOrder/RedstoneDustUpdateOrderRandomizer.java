/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.redstoneDustRandomUpdateOrder;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RedstoneDustUpdateOrderRandomizer
{
	private static final Random RANDOM = new Random();
	public static RandomizerType randomizer = RandomizerType.NONE;

	public static void syncRule(String newValue)
	{
		RedstoneDustUpdateOrderRandomizer.randomizer = RandomizerType.fromString(newValue).orElseThrow(() -> new RuntimeException("Unknown RandomizerType " + newValue));
	}

	public static boolean isRuleEnabled()
	{
		return randomizer.willModifyUpdateOrder();
	}

	public static void shuffle(List<BlockPos> updateOrder, BlockPos dustPos, Level world)
	{
		if (updateOrder.size() < 2)
		{
			return;
		}
		switch (randomizer)
		{
			case NONE:
				break;
			case RANDOM_SHUFFLE:
				Collections.shuffle(updateOrder, RANDOM);
				break;
			case RANDOM_POSITION:
				BlockPos samplePos = getRandomPosInWorld(world);

				// Match vanilla insertion order for positions in the same hash bucket.
				Set<BlockPos> affectedNeighbors = Sets.newHashSet();
				affectedNeighbors.add(samplePos);
				for (Direction direction : Direction.values())
				{
					affectedNeighbors.add(samplePos.relative(direction));
				}

				int next = 0;
				for (BlockPos sampleTarget : affectedNeighbors)
				{
					BlockPos actualTarget = dustPos.offset(sampleTarget.subtract(samplePos));
					int index = updateOrder.indexOf(actualTarget);
					if (index >= 0)
					{
						Collections.swap(updateOrder, next++, index);
					}
				}
				break;
			default:
				throw new RuntimeException("Unexpected randomizer " + randomizer);
		}
	}

	private static BlockPos getRandomPosInWorld(Level world)
	{
		final int MAX_WIDTH = 30_000_000;
		int x = RANDOM.nextInt(MAX_WIDTH * 2) - MAX_WIDTH;
		int z = RANDOM.nextInt(MAX_WIDTH * 2) - MAX_WIDTH;

		//#if MC >= 12102
		//$$ int y = world.getMinY() + RANDOM.nextInt(world.getHeight());
		//#elseif MC >= 11700
		//$$ int y = world.getMinBuildHeight() + RANDOM.nextInt(world.getHeight());
		//#else
		int y = RANDOM.nextInt(256);
		//#endif

		return new BlockPos(x, y, z);
	}
}
