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

package carpettisaddition.mixins.rule.flattenTriangularDistribution;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(RandomSource.class)
public interface RandomMixin
{
	@Shadow double nextDouble();

	@Inject(method = "triangle(DD)D", at = @At("HEAD"), cancellable = true)
	default void nextTriangular_double(double mode, double deviation, CallbackInfoReturnable<Double> cir)
	{
		if (CarpetTISAdditionSettings.flattenTriangularDistribution)
		{
			this.nextDouble();
			cir.setReturnValue(mode + deviation * (-1 + this.nextDouble() * 2));
		}
	}

	//#if MC >= 1.21.2
	//$$ @Shadow float nextFloat();
	//$$
	//$$ @Inject(method = "triangle(FF)F", at = @At("HEAD"), cancellable = true)
	//$$ default void nextTriangular_float(float mode, float deviation, CallbackInfoReturnable<Float> cir)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.flattenTriangularDistribution)
	//$$ 	{
	//$$ 		this.nextFloat();
	//$$ 		cir.setReturnValue(mode + deviation * (-1 + this.nextFloat() * 2));
	//$$ 	}
	//$$ }
	//#endif
}
