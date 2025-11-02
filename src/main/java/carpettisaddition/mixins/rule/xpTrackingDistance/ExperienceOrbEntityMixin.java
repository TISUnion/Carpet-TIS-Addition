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

package carpettisaddition.mixins.rule.xpTrackingDistance;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ExperienceOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbEntityMixin
{
	@ModifyExpressionValue(
			//#if MC >= 12105
			//$$ method = "followNearbyPlayer",
			//#elseif MC >= 11700
			//$$ method = {"tick", "scanForEntities"}, require = 1,
			//#else
			method = "tick", require = 3,
			//#endif
			at = @At(
					value = "CONSTANT",
					args = "doubleValue=8.0D"
			)
	)
	private double xpTrackingDistance_modifyGiveUpDistance(double value)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance;
	}

	@ModifyExpressionValue(
			//#if MC >= 12105
			//$$ method = "followNearbyPlayer",
			//#elseif MC >= 11700
			//$$ method = {"tick", "scanForEntities"}, require = 1,
			//#else
			method = "tick", require = 2,
			//#endif
			at = @At(
					value = "CONSTANT",
					args = "doubleValue=64.0D"
			)
	)
	private double xpTrackingDistance_modifyGiveUpDistanceSquare(double value)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance * CarpetTISAdditionSettings.xpTrackingDistance;
	}

	@ModifyExpressionValue(
			//#if MC >= 12105
			//$$ method = "followNearbyPlayer",
			//#else
			method = "tick",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;isSpectator()Z"
			)
	)
	private boolean xpTrackingDistance_isSpectatorOrTrackingDistanceIsZero(boolean isSpectator)
	{
		return isSpectator || CarpetTISAdditionSettings.xpTrackingDistance == 0;
	}
}
