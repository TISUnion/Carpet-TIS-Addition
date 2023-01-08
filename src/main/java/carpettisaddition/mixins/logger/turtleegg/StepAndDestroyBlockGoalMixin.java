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

package carpettisaddition.mixins.logger.turtleegg;

import carpettisaddition.logging.loggers.turtleegg.TurtleEggLogger;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.StepAndDestroyBlockGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(StepAndDestroyBlockGoal.class)
public abstract class StepAndDestroyBlockGoalMixin
{
	@Shadow @Final private MobEntity stepAndDestroyMob;

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void dontBreakTheEgg(CallbackInfo ci, World world, BlockPos blockPos, BlockPos blockPos2)
	{
		if (TurtleEggLogger.getInstance().isActivated())
		{
			BlockState blockState = world.getBlockState(blockPos2);
			if (blockState.getBlock() == Blocks.TURTLE_EGG)
			{
				TurtleEggLogger.getInstance().onBreakingEgg(world, blockPos2, blockState, this.stepAndDestroyMob);
			}
		}
	}
}
