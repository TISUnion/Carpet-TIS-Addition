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

package carpettisaddition.mixins.rule.explosionNoEntityInfluence;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collections;
import java.util.List;

//#if MC >= 12102
//$$ import net.minecraft.world.level.ServerExplosion;
//#else
import net.minecraft.world.level.Explosion;
//#endif

@Mixin(
		//#if MC >= 12102
		//$$ ServerExplosion.class
		//#else
		Explosion.class
		//#endif
)
public abstract class ExplosionMixin
{
	@ModifyExpressionValue(
			//#if MC >= 12102
			//$$ method = "hurtEntities",
			//#else
			method = "explode",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12102
					//$$ target = "Lnet/minecraft/server/level/ServerLevel;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
					//#else
					target = "Lnet/minecraft/world/level/Level;getEntities(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"
					//#endif
			)
	)
	private List<Entity> explosionNoEntityInfluence(List<Entity> entities)
	{
		if (CarpetTISAdditionSettings.explosionNoEntityInfluence)
		{
			entities = Collections.emptyList();
		}
		return entities;
	}
}
