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
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ExperienceOrbEntity.class)
public abstract class ExperienceOrbEntityMixin
{
	@ModifyConstant(
			//#if MC >= 11700
			//$$ method = {"tick", "expensiveUpdate"},
			//$$ require = 1,
			//#else
			method = "tick",
			require = 3,
			//#endif
			constant = @Constant(doubleValue = 8.0D)
	)
	private double modifyGiveUpDistance(double value)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance;
	}

	@ModifyConstant(
			//#if MC >= 11700
			//$$ method = {"tick", "expensiveUpdate"},
			//$$ require = 1,
			//#else
			method = "tick",
			require = 2,
			//#endif
			constant = @Constant(doubleValue = 64.0D)
	)
	private double modifyGiveUpDistanceSquare(double value)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance * CarpetTISAdditionSettings.xpTrackingDistance;
	}

	@Redirect(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z"
			)
	)
	private boolean isSpectatorOrTrackingDistanceIsZero(PlayerEntity player)
	{
		return CarpetTISAdditionSettings.xpTrackingDistance == 0 || player.isSpectator();
	}
}
