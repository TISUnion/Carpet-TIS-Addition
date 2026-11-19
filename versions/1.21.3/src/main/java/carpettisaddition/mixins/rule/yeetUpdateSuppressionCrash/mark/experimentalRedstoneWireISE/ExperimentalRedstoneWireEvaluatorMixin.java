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

package carpettisaddition.mixins.rule.yeetUpdateSuppressionCrash.mark.experimentalRedstoneWireISE;

import carpettisaddition.helpers.rule.yeetUpdateSuppressionCrash.UpdateSuppressionYeeter;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.redstone.ExperimentalRedstoneWireEvaluator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc >= 1.21.2: subproject 1.21.3                 <--------
 * mc <  1.21.2: subproject 1.15.2 (main project)
 */
@Mixin(ExperimentalRedstoneWireEvaluator.class)
public abstract class ExperimentalRedstoneWireEvaluatorMixin
{
	@ModifyExpressionValue(
			method = "calculateCurrentChanges",
			at = @At(
					value = "NEW",
					target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;"
			),
			allow = 1
	)
	private IllegalStateException yeetUpdateSuppressionCrash_wrapExperimentalWireIllegalStateSuppression(
			IllegalStateException throwable,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos initialPos
	)
	{
		return (IllegalStateException)UpdateSuppressionYeeter.tryReplaceWithWrapper(throwable, world, initialPos);
	}
}
