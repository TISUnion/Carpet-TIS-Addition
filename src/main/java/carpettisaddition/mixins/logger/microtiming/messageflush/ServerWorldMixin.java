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

package carpettisaddition.mixins.logger.microtiming.messageflush;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin
{
	// tickTime() call is still the most accurate place to hook at
	// but mc1.20.3+ vanilla /tick freeze wraps tickTime() with an if-statement, making it no longer stable
	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12003
					//$$ target = "Lnet/minecraft/server/level/ServerLevel;updateSkyBrightness()V",
					//$$ shift = At.Shift.AFTER
					//#else
					target = "Lnet/minecraft/server/level/ServerLevel;tickTime()V"
					//#endif
			)
	)
	private void flushMessageOnTimeUpdate(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.microTimingTickDivision == TickDivision.WORLD_TIMER)
		{
			if (DimensionWrapper.of((ServerLevel)(Object)this).equals(DimensionWrapper.OVERWORLD))  // only flush messages at overworld time update
			{
				MicroTimingLoggerManager.flushMessages();
			}
		}
	}
}
