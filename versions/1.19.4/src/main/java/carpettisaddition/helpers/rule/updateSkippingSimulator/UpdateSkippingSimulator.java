/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.updateSkippingSimulator;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.mixins.rule.updateSkippingSimulator.ChainRestrictedNeighborUpdaterAccessor;
import carpettisaddition.mixins.rule.updateSkippingSimulator.WorldAccessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UpdateSkippingSimulator
{
	public static boolean isActivated()
	{
		return CarpetTISAdditionSettings.updateSkippingSimulator;
	}

	public static void kaboom(World world, BlockPos pos)
	{
		var neighborUpdater = ((WorldAccessor)world).getNeighborUpdater$TISCM();
		if (neighborUpdater instanceof ChainRestrictedNeighborUpdaterAccessor accessor)
		{
			int nowDepth = accessor.getDepth$TISCM();
			int maxDepth = accessor.getMaxChainDepth$TISCM();
			if (nowDepth < maxDepth)
			{
				accessor.setDepth$TISCM(maxDepth);
			}
		}
	}
}
