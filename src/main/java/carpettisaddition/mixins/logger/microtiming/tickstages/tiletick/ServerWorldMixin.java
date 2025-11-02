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

package carpettisaddition.mixins.logger.microtiming.tickstages.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin
{
	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/server/level/ServerLevel;blockTicks:Lnet/minecraft/world/ticks/LevelTicks;"
					//#else
					target = "Lnet/minecraft/server/level/ServerLevel;blockTicks:Lnet/minecraft/world/level/ServerTickList;"
					//#endif
			)
	)
	private void enterStageTileTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerLevel)(Object)this, TickStage.TILE_TICK);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							//#if MC >= 11800
							//$$ target = "Lnet/minecraft/server/level/ServerLevel;fluidTicks:Lnet/minecraft/world/ticks/LevelTicks;"
							//#else
							target = "Lnet/minecraft/server/level/ServerLevel;liquidTicks:Lnet/minecraft/world/level/ServerTickList;"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/world/ticks/LevelTicks;tick(JILjava/util/function/BiConsumer;)V",
					//#else
					target = "Lnet/minecraft/world/level/ServerTickList;tick()V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void exitStageTileTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerLevel)(Object)this, TickStage.UNKNOWN);
	}
}
