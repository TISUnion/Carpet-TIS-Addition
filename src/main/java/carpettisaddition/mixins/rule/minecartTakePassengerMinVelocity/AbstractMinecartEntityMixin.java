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
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 1.21.2
//$$ import net.minecraft.world.entity.vehicle.OldMinecartBehavior;
//#else
import net.minecraft.world.entity.vehicle.AbstractMinecart;
//#endif

@Mixin(
		//#if MC >= 1.21.2
		//$$ OldMinecartBehavior.class
		//#else
		AbstractMinecart.class
		//#endif
)
public abstract class AbstractMinecartEntityMixin
{
	@ModifyExpressionValue(
			//#if MC >= 1.21.2
			//$$ method = "handleCollision",
			//#else
			method = "tick",
			//#endif
			// cannot use our 0.1D constant value here, since 0.1D * 0.1D != 0.01D
			at = @At(
					value = "CONSTANT",
					args = "doubleValue=0.01"
			)
	)
	private double minecartTakePassengerMinVelocity_impl(double squaredThreshold)
	{
		if (CarpetTISAdditionSettings.minecartTakePassengerMinVelocity != CarpetTISAdditionSettings.VANILLA_MINECART_TAKE_PASSENGER_MIN_VELOCITY)
		{
			// since the operator is <, but what we want is <=, here comes a Math.nextDown trick
			squaredThreshold = Math.nextDown(CarpetTISAdditionSettings.minecartTakePassengerMinVelocity);
		}
		return squaredThreshold;
	}
}
