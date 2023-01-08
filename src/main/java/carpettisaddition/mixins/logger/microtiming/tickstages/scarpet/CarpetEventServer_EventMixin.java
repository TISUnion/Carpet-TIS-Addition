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

package carpettisaddition.mixins.logger.microtiming.tickstages.scarpet;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		targets = {
				"carpet.script.CarpetEventServer$Event$1",  // TICK
				"carpet.script.CarpetEventServer$Event$2",  // NETHER_TICK
				"carpet.script.CarpetEventServer$Event$3",  // ENDER_TICK
		},
		remap = false
)
public abstract class CarpetEventServer_EventMixin
{
	/**
	 * require = 0 for preventing it from crashing if carpet changes the order of anonymous classes
	 * scarpet stage it's not that important so failure is tolerable
	 */
	@Inject(method = "onTick", at = @At("HEAD"), remap = false, require = 0)
	private void enterCarpetModStage(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.SCARPET);
	}

	@Inject(method = "onTick", at = @At("HEAD"), remap = false, require = 0)
	private void exitCarpetModStage(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.UNKNOWN);
	}
}
