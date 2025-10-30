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
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonEntityMixin
{
	@ModifyArg(
			method = "aiStep",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12100
					//$$ target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;setOnFireFor(F)V"
					//#else
					target = "Lnet/minecraft/world/entity/monster/AbstractSkeleton;setSecondsOnFire(I)V"
					//#endif
			)
	)
	//#if MC >= 12100
	//$$ private float undeadDontBurnInSunlight_skeleton(float duration)
	//#else
	private int undeadDontBurnInSunlight_skeleton(int duration)
	//#endif
	{
		if (CarpetTISAdditionSettings.undeadDontBurnInSunlight)
		{
			duration = 0;
		}
		return duration;
	}
}
