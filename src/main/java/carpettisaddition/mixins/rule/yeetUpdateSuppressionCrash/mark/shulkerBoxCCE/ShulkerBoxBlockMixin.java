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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.mark.shulkerBoxCCE;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionException;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11600
//$$ import net.minecraft.world.level.block.state.BlockBehaviour;
//#else
import net.minecraft.world.level.block.state.BlockState;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ BlockBehaviour.BlockStateBase.class
		//#else
		BlockState.class
		//#endif
)
public abstract class ShulkerBoxBlockMixin
{
	@WrapOperation(
			method = "getAnalogOutputSignal",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/Block;getAnalogOutputSignal(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I"
			)
	)
	private int yeetUpdateSuppressionCrash_wrapShulkerBoxClassCastException(Block instance, BlockState blockState, Level level, BlockPos blockPos, Operation<Integer> original) throws Throwable
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash && instance instanceof ShulkerBoxBlock)
		{
			try
			{
				return original.call(instance, blockState, level, blockPos);
			}
			catch (Throwable throwable)
			{
				if (throwable instanceof UpdateSuppressionException || throwable instanceof ClassCastException)
				{
					throw UpdateSuppressionYeeter.tryReplaceWithWrapper(throwable, level, blockPos);
				}
				throw throwable;
			}
		}
		else
		{
			// vanilla
			return  original.call(instance, blockState, level, blockPos);
		}
	}
}
