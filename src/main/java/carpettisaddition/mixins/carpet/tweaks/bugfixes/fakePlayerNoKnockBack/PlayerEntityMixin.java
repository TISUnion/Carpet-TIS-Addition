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

package carpettisaddition.mixins.carpet.tweaks.bugfixes.fakePlayerNoKnockBack;

import carpet.patches.EntityPlayerMPFake;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * lower priority so it will more likely be mixin later
 * this issue is fixed in fabric-carpet v1.4.33 (mc1.16)
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(value = PlayerEntity.class, priority = 500)
public abstract class PlayerEntityMixin
{
	@ModifyExpressionValue(
			method = "attack",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/Entity;velocityModified:Z",
					ordinal = 0
			)
	)
	private boolean velocityModifiedAndNotCarpetFakePlayer(boolean velocityModified, @Local(argsOnly = true) Entity target)
	{
		return velocityModified && !(target instanceof EntityPlayerMPFake);
	}
}
