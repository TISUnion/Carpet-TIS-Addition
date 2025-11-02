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

package carpettisaddition.mixins.logger.microtiming.events.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.interfaces.ITileTickListWithServerWorld;
import carpettisaddition.utils.WorldUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.LevelTicks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LevelTicks.class)
public abstract class TileTickListMixin<T>
{
	private int oldListSize;

	@Inject(
			method = "schedule",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/ticks/LevelChunkTicks;schedule(Lnet/minecraft/world/ticks/ScheduledTick;)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void startScheduleTileTickEvent(ScheduledTick<T> orderedTick, CallbackInfo ci, long l, LevelChunkTicks<T> chunkTickScheduler)
	{
		this.oldListSize = chunkTickScheduler.count();
	}

	@Inject(
			method = "schedule",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/ticks/LevelChunkTicks;schedule(Lnet/minecraft/world/ticks/ScheduledTick;)V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void endScheduleTileTickEvent(ScheduledTick<T> tt, CallbackInfo ci, long l, LevelChunkTicks<T> chunkTickScheduler)
	{
		ServerLevel serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			int delay = (int)(tt.triggerTick() - WorldUtils.getWorldTime(serverWorld));
			MicroTimingLoggerManager.onScheduleTileTickEvent(serverWorld, tt.type(), tt.pos(), delay, tt.priority(), chunkTickScheduler.count() > this.oldListSize);
		}
	}
	
	// execute tile tick events, for mc1.18+

	@ModifyVariable(
			method = "runCollectedTicks",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					remap = false
			)
	)
	private ScheduledTick<T> preExecuteBlockTileTickEvent(ScheduledTick<T> event)
	{
		ServerLevel serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_START);
		}
		return event;
	}

	@ModifyVariable(
			method = "runCollectedTicks",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					shift = At.Shift.AFTER,
					remap = false
			)
	)
	private ScheduledTick<T> postExecuteBlockTileTickEvent(ScheduledTick<T> event)
	{
		ServerLevel serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_END);
		}
		return event;
	}
}
