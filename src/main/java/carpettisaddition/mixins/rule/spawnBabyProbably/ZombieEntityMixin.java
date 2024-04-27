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

package carpettisaddition.mixins.rule.spawnBabyProbably;

import carpettisaddition.helpers.rule.spawnBabyProbably.SpawnBabyProbablyHelper;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin
{
	@ModifyArg(
			method = "initialize",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					target = "Lnet/minecraft/entity/mob/ZombieEntity$ZombieData;<init>(ZZ)V"
					//#else
					//$$ target = "Lnet/minecraft/entity/mob/ZombieEntity$Data;<init>(Lnet/minecraft/entity/mob/ZombieEntity;ZLnet/minecraft/entity/mob/ZombieEntity$1;)V"
					//#endif
			)
			//#if MC >= 11600
			, index = 0
			//#endif
	)
	private boolean spawnBabyProbably_zombie(boolean isBaby)
	{
		return SpawnBabyProbablyHelper.tweak(isBaby, true, false);
	}
}
