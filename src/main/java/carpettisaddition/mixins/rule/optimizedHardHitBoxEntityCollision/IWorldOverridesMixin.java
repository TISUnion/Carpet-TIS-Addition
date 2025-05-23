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

package carpettisaddition.mixins.rule.optimizedHardHitBoxEntityCollision;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.mixin.IWorldOverrides;
import carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision.OptimizedHardHitBoxEntityCollisionHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(conflict = @Condition(ModIds.async))
@Mixin(IWorldOverrides.class)
public abstract class IWorldOverridesMixin
{
	@Inject(method = "getEntityCollisionsPre", at = @At("HEAD"), remap = false)
	private static void optimizedHardHitBoxEntityCollision_enableFlag(Entity entity, Box box, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision)
		{
			if (!OptimizedHardHitBoxEntityCollisionHelper.treatsGeneralEntityAsHardHitBox(entity))
			{
				OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.set(true);
			}
		}
	}

	@Inject(method = "getEntityCollisionsPost", at = @At("HEAD"), remap = false)
	private static void optimizedHardHitBoxEntityCollision_disableFlag(Entity entity, Box box, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.optimizedHardHitBoxEntityCollision)
		{
			OptimizedHardHitBoxEntityCollisionHelper.checkHardHitBoxEntityOnly.set(false);
		}
	}
}
