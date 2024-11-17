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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.mark;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * In versions of Minecraft prior to 1.16, stack overflow errors caused purely by state update chains
 * would not convert {@link StackOverflowError} into {@link net.minecraft.util.crash.CrashException}.
 * This meant that the catch in TaskExecutor could not capture this stack overflow exception,
 * leading to a server crash.
 * <p>
 * Here is a fix for this specific scenario
 * <p>
 * Starting from Minecraft 1.16, a maxUpdates mechanism was introduced for pure state update chain
 * to control the maximum recursion depth, so this issue is unlikely to occur anymore (hopefully)
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(BlockState.class)
public abstract class BlockStateMixin
{
	@WrapOperation(
			method = "updateNeighborStates",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/Block;updateNeighborStates(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;I)V"
			)
	)
	private void yeetUpdateSuppressionCrash_wrapStackOverflow_stateUpdate(Block instance, BlockState blockState, IWorld iWorld, BlockPos pos, int flags, Operation<Void> original) throws Throwable
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			try
			{
				original.call(instance, blockState, iWorld, pos, flags);
			}
			catch (Throwable throwable)
			{
				throw UpdateSuppressionYeeter.tryReplaceWithWrapper(throwable, iWorld.getWorld(), pos);
			}
		}
		else
		{
			// vanilla
			original.call(instance, blockState, iWorld, pos, flags);
		}
	}
}
