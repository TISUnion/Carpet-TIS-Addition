/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.breedingCooldownDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.AgableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgableMob.class)
public abstract class PassiveEntityMixin
{
	@Shadow
	protected int age;

	@Inject(method = "setAge", at = @At("HEAD"), cancellable = true)
	private void breedingCooldownDisabled_cancelIfValueIsGreaterThanZero(CallbackInfo ci, @Local(argsOnly = true) int newAge)
	{
		if (CarpetTISAdditionSettings.breedingCooldownDisabled)
		{
			// age >= 0: adult, value is breeding cooldown
			// age < 0: baby, value is age
			if (this.age == 0 && newAge > 0)  // adult, setting breeding cooldown from none to *newAge*
			{
				ci.cancel();
			}
		}
	}
}
