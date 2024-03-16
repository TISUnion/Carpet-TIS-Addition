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

package carpettisaddition.mixins.rule.creativeInstantTame;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.EntityUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WolfEntity.class)
public abstract class WolfEntityMixin
{
	@ModifyExpressionValue(
			//#if MC >= 12005
			//$$ method = "method_58169",
			//#else
			method = "interactMob",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11900
					//$$ target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I",
					//#else
					target = "Ljava/util/Random;nextInt(I)I",
					//#endif
					ordinal = 0
			)
	)
	private int creativeInstantTame_wolf(int value, @Local(argsOnly = true) PlayerEntity player)
	{
		if (CarpetTISAdditionSettings.creativeInstantTame && EntityUtil.isCreativePlayer(player))
		{
			value = 0;
		}
		return value;
	}
}
