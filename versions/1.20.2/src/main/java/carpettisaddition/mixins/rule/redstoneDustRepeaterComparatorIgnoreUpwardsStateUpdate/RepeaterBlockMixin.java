/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.redstoneDustRepeaterComparatorIgnoreUpwardsStateUpdate;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.compat.DummyClass;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.RepeaterBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RepeaterBlock.class)
public abstract class RepeaterBlockMixin
{
	@ModifyExpressionValue(
			method = "getStateForNeighborUpdate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/RepeaterBlock;canPlaceAbove(Lnet/minecraft/world/WorldView;Lnet/minecraft/core/BlockPos;Lnet/minecraft/block/BlockState;)Z"
			),
			allow = 1
	)
	private boolean redstoneDustRepeaterComparatorIgnoreUpwardsStateUpdate_repeater(boolean canPlaceAbove)
	{
		if (CarpetTISAdditionSettings.redstoneDustRepeaterComparatorIgnoreUpwardsStateUpdate)
		{
			canPlaceAbove = true;
		}
		return canPlaceAbove;
	}
}
