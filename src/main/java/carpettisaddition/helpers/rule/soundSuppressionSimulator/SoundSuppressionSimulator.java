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

package carpettisaddition.helpers.rule.soundSuppressionSimulator;

import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

//#if MC >= 1.18
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//#endif

public class SoundSuppressionSimulator
{
	public static boolean isActivated()
	{
		//#if MC >= 1.18
		//$$ return CarpetTISAdditionSettings.soundSuppressionSimulator;
		//#else
		return false;
		//#endif
	}

	/**
	 * The same exception type as {@link net.minecraft.world.level.block.state.StateHolder#getValue}
	 */
	public static void kaboom(Level world, BlockPos pos) throws Throwable
	{
		Throwable e = new IllegalArgumentException("TISCM SoundSuppressionSimulator");
		e = UpdateSuppressionYeeter.tryReplaceWithWrapper(e, world, pos);
		throw e;
	}
}
