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
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileTickSubStage;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.TickNextTickData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.18"))
@Mixin(ServerLevel.class)
public abstract class ServerWorld_TileTickDetailMixin
{
	private int tileTickOrderCounter = 0;

	@Inject(
			method = "tick",
			at = {
					@At(
							value = "FIELD",
							target = "Lnet/minecraft/server/level/ServerLevel;blockTicks:Lnet/minecraft/world/level/ServerTickList;"
					),
					@At(
							value = "FIELD",
							target = "Lnet/minecraft/server/level/ServerLevel;liquidTicks:Lnet/minecraft/world/level/ServerTickList;"
					)
			}
	)
	private void resetTileTickOrderCounter(CallbackInfo ci)
	{
		this.tileTickOrderCounter = 0;
	}

	@Inject(method = {"tickBlock", "tickLiquid"}, at = @At("HEAD"))
	private void beforeExecuteTileTickEvent(TickNextTickData<?> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerLevel)(Object)this, String.valueOf(event.priority.getValue()));
		MicroTimingLoggerManager.setSubTickStage((ServerLevel)(Object)this, new TileTickSubStage((ServerLevel)(Object)this, event, this.tileTickOrderCounter++));
	}

	@Inject(method = {"tickBlock", "tickLiquid"}, at = @At("RETURN"))
	private void afterExecuteTileTickEvent(TickNextTickData<?> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerLevel)(Object)this, null);
		MicroTimingLoggerManager.setSubTickStage((ServerLevel)(Object)this, null);
	}
}
