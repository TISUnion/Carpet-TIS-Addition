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

package carpettisaddition.mixins.rule.minecartTakePassengerMinVelocity;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin
{
	@ModifyConstant(
			method = "tick",
			// cannot use our 0.1D constant value here, since 0.1D * 0.1D != 0.01D
			constant = @Constant(doubleValue = 0.01D),
			require = 0
	)
	private double minecartTakePassengerMinVelocity(double squaredThreshold)
	{
		if (CarpetTISAdditionSettings.minecartTakePassengerMinVelocity != CarpetTISAdditionSettings.VANILLA_MINECART_TAKE_PASSENGER_MIN_VELOCITY)
		{
			// since the operator is <, but what we want is <=, here comes a Math.nextDown trick
			squaredThreshold = Math.nextDown(CarpetTISAdditionSettings.minecartTakePassengerMinVelocity);
		}
		return squaredThreshold;
	}
}
