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
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11600
//$$ import net.minecraft.state.State;
//#else
import net.minecraft.world.level.block.state.AbstractStateHolder;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ State.class
		//#else
		AbstractStateHolder.class
		//#endif
)
public abstract class AbstractStateMixin
{
	@SuppressWarnings("unchecked")
	@ModifyReturnValue(method = "getValue(Lnet/minecraft/world/level/block/state/properties/Property;)Ljava/lang/Comparable;", at = @At("TAIL"))
	private <T extends Comparable<T>> T tweaksGetStateResultForLargeBarrel(T ret)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			if (LargeBarrelHelper.applyAxisOnlyDirectionTesting.get())
			{
				if (ret instanceof Direction)
				{
					Direction direction = (Direction)ret;
					return (T)Direction.fromAxisAndDirection(direction.getAxis(), Direction.AxisDirection.NEGATIVE);
				}
			}
		}
		return ret;
	}
}
