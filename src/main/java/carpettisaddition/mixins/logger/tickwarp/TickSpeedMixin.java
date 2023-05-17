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

package carpettisaddition.mixins.logger.tickwarp;

import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import net.minecraft.text.BaseText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 12000
//$$ import carpet.helpers.TickRateManager;
//#else
import carpet.helpers.TickSpeed;
//#endif

@Mixin(
		//#if MC >= 12000
		//$$ TickRateManager.class
		//#else
		TickSpeed.class
		//#endif
)
public abstract class TickSpeedMixin
{
	@Inject(method = "tickrate_advance", at = @At("TAIL"), remap = false)
	private
	//#if MC < 12000
	static
	//#endif
	void recordTickWarpAdvancer(CallbackInfoReturnable<BaseText> cir)
	{
		TickWarpHUDLogger.getInstance().recordTickWarpAdvancer();
	}

	@Inject(method = "finish_time_warp", at = @At("HEAD"), remap = false)
	private
	//#if MC < 12000
	static
	//#endif
	void recordTickWarpResult(CallbackInfo ci)
	{
		TickWarpHUDLogger.getInstance().recordTickWarpResult();
	}
}
