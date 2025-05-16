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

package carpettisaddition.mixins.rule.wetExplosionReintroduced;

import carpettisaddition.helpers.rule.wetExplosionReintroduced.WetExplosionReintroducedUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 12102
//$$ import net.minecraft.world.explosion.ExplosionImpl;
//#endif

@Mixin(
		//#if MC >= 12102
		//$$ ExplosionImpl.class
		//#else
		Explosion.class
		//#endif
)
public abstract class ExplosionMixin
{
	@ModifyExpressionValue(
			//#if MC >= 12102
			//$$ method = "damageEntities",
			//#else
			method = "collectBlocksAndDamageEntities",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12004
					//$$ target = "Lnet/minecraft/entity/Entity;isImmuneToExplosion(Lnet/minecraft/world/explosion/Explosion;)Z"
					//#else
					target = "Lnet/minecraft/entity/Entity;isImmuneToExplosion()Z"
					//#endif
			)
	)
	private boolean wetExplosionReintroduced_vanilla(
			boolean isImmune,
			@Local Entity entity
	)
	{
		if (WetExplosionReintroducedUtils.check((Explosion)(Object)this, entity))
		{
			isImmune = true;
		}
		return isImmune;
	}
}
