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

package carpettisaddition.mixins.rule.farmlandTrampledDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.FarmlandBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmlandBlock.class)
public abstract class FarmLandBlockMixin
{
	@ModifyExpressionValue(
			method = "onLandedUpon",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/World;isClient:Z"
			)
	)
	private boolean farmlandTrampledDisabled_cancelIfStatement(boolean isClient)
	{
		if (CarpetTISAdditionSettings.farmlandTrampledDisabled)
		{
			// world.isClient returning true -> if predicate fails -> farmland protected
			isClient = true;
		}
		return isClient;
	}
}
