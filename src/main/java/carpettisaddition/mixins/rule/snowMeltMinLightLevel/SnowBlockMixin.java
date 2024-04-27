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

package carpettisaddition.mixins.rule.snowMeltMinLightLevel;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.SnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SnowBlock.class)
public abstract class SnowBlockMixin
{
	@ModifyConstant(
			//#if MC >= 11600
			method = "randomTick",
			//#elseif MC >= 11500
			//$$ method = "scheduledTick",
			//#else
			//$$ method = "onScheduledTick",
			//#endif
			constant = @Constant(intValue = CarpetTISAdditionSettings.VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL - 1),
			require = 0
	)
	private int snowMeltMinLightLevel(int lightLevel)
	{
		if (CarpetTISAdditionSettings.snowMeltMinLightLevel != CarpetTISAdditionSettings.VANILLA_SNOW_MELT_MIN_LIGHT_LEVEL)
		{
			return CarpetTISAdditionSettings.snowMeltMinLightLevel - 1;
		}
		return lightLevel;
	}
}
