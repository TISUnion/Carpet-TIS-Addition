/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.wetExplosionReintroduced.compat.carpet;

import carpet.helpers.OptimizedExplosion;
import carpettisaddition.helpers.rule.wetExplosionReintroduced.WetExplosionReintroducedUtils;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// carpet removed the damage-entity codes in mc1.21.2+
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<=1.21.1"))
@Mixin(OptimizedExplosion.class)
public abstract class OptimizedExplosionMixin
{
	@ModifyExpressionValue(
			method = "doExplosionA",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12004
					//$$ target = "Lnet/minecraft/world/entity/Entity;ignoreExplosion(Lnet/minecraft/world/level/Explosion;)Z"
					//#else
					target = "Lnet/minecraft/world/entity/Entity;ignoreExplosion()Z"
					//#endif
			)
	)
	private static boolean wetExplosionReintroduced_carpet(
			boolean isImmune,
			@Local(argsOnly = true) Explosion explosion,
			//#if MC >= 11600
			//$$ @Local(ordinal = 1) Entity entity
			//#else
			@Local Entity entity
			//#endif
	)
	{
		if (WetExplosionReintroducedUtils.check(explosion, entity))
		{
			isImmune = true;
		}
		return isImmune;
	}
}
