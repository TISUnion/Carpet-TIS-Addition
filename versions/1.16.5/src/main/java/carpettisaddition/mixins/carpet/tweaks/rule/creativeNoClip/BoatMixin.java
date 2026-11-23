/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 *           mc >= 1.17.1: subproject 1.17.1
 * 1.16.4 <= mc <= 1.16.5: subproject 1.16.5  <--------
 *           mc <= 1.15.2: subproject 1.15.2 (main project)
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16 <1.17"))
@Mixin(Boat.class)
public abstract class BoatMixin
{
	// Boats and minecarts share this check, which does not use NO_SPECTATORS in 1.16
	@ModifyReturnValue(method = "canVehicleCollide", at = @At("RETURN"))
	private static boolean ignoreCreativeNoClipPlayer(boolean original, Entity vehicle, Entity target)
	{
		return original && !CreativeNoClipHelper.isNoClipPlayer(target);
	}
}
