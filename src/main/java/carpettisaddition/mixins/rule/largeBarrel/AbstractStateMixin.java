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

package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11600
import net.minecraft.state.State;
//#else
//$$ import net.minecraft.state.AbstractState;
//#endif

@Mixin(
		//#if MC >= 11600
		State.class
		//#else
		//$$ AbstractState.class
		//#endif
)
public abstract class AbstractStateMixin
{
	@SuppressWarnings("unchecked")
	@Inject(method = "get", at = @At("TAIL"), cancellable = true)
	private <T extends Comparable<T>> void tweaksGetStateResultForLargeBarrel(CallbackInfoReturnable<T> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (LargeBarrelHelper.applyAxisOnlyDirectionTesting.get())
			{
				if (cir.getReturnValue() instanceof Direction)
				{
					Direction direction = (Direction) cir.getReturnValue();
					cir.setReturnValue((T)Direction.from(direction.getAxis(), Direction.AxisDirection.NEGATIVE));
				}
			}
		}
	}
}
