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

package carpettisaddition.mixins.rule.spawnJockeyProbably;

import carpettisaddition.helpers.rule.spawnJockeyProbably.SpawnJockeyProbablyRandomizer;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.monster.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Zombie.class)
public abstract class ZombieEntityMixin
{
	@ModifyExpressionValue(
			method = "finalizeSpawn",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/entity/monster/Zombie;setBaby(Z)V"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11900
					//$$ target = "Lnet/minecraft/util/RandomSource;nextFloat()F",
					//#else
					target = "Ljava/util/Random;nextFloat()F",
					//#endif
					ordinal = 1
			)
	)
	private float spawnJockeyProbably_tweakChickenJockey(float value)
	{
		return SpawnJockeyProbablyRandomizer.tweak(value, 0.0F, 1.0F);
	}
}
