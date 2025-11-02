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

package carpettisaddition.mixins.rule.turtleEggTrampledDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RemoveBlockGoal.class)
public abstract class StepAndDestroyBlockGoalMixin
{
	@Shadow private int ticksSinceReachedGoal;

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11600
							//$$ target = "Lnet/minecraft/world/entity/ai/goal/RemoveBlockGoal;playDestroyProgressSound(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V"
							//#else
							target = "Lnet/minecraft/world/entity/ai/goal/RemoveBlockGoal;playDestroyProgressSound(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)V"
							//#endif
					)
			),
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/entity/ai/goal/RemoveBlockGoal;ticksSinceReachedGoal:I"
			)
	)
	private void dontBreakTheEgg(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.turtleEggTrampledDisabled)
		{
			if (this.ticksSinceReachedGoal > 60)
			{
				this.ticksSinceReachedGoal -= 30;
			}
		}
	}
}
