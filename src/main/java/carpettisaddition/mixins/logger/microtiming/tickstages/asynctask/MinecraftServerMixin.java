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

package carpettisaddition.mixins.logger.microtiming.tickstages.asynctask;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.playeraction.NetworkThreadUtilsMixin;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Inject(
			//#if MC >= 11700
			method = "runTasksTillTickEnd",
			//#else
			//$$ method = "method_16208",
			//#endif
			at = @At("HEAD")
	)
	private void enterStageAsyncTask(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.ASYNC_TASK);
	}

	@Inject(
			//#if MC >= 11700
			method = "runTasksTillTickEnd",
			//#else
			//$$ method = "method_16208",
			//#endif
			at = @At("TAIL")
	)
	private void exitStageAsyncTask(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.UNKNOWN);
	}

	/**
	 * Reset potential tick stage (extra) set in {@link NetworkThreadUtilsMixin}
	 */
	@Inject(method = "runTask", at = @At("RETURN"))
	void cleanSubStageInStagePlayerAction(CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.ASYNC_TASK);
	}
}
