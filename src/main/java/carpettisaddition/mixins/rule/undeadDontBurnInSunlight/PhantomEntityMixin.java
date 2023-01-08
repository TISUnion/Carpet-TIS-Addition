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

package carpettisaddition.mixins.rule.undeadDontBurnInSunlight;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin
{
	@ModifyArg(
			method = "tickMovement",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/PhantomEntity;setOnFireFor(I)V"
			)
	)
	private int undeadDontBurnInSunlight_phantom(int duration)
	{
		if (CarpetTISAdditionSettings.undeadDontBurnInSunlight)
		{
			duration = 0;
		}
		return duration;
	}
}
