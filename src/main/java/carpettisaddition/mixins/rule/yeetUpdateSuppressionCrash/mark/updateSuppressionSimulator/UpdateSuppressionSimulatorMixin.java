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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.mark.updateSuppressionSimulator;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.updateSuppressionSimulator.UpdateSuppressionSimulator;
import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(UpdateSuppressionSimulator.class)
public abstract class UpdateSuppressionSimulatorMixin
{
	@WrapOperation(
			method = "kaboom",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Runnable;run()V"
			),
			remap = false
	)
	private static void yeetUpdateSuppressionCrash_wrapUpdateSuppressionSimulator(
			Runnable instance, Operation<Void> original,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos pos
	) throws Throwable
	{
		if (CarpetTISAdditionSettings.yeetUpdateSuppressionCrash)
		{
			try
			{
				original.call(instance);
			}
			catch (Throwable throwable)
			{
				throw UpdateSuppressionYeeter.tryReplaceWithWrapper(throwable, world, pos);
			}
		}
		else
		{
			original.call(instance);
		}
	}
}
