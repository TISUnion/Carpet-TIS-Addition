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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.WorldTickScheduler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldTickScheduler.class)
public abstract class TileTickListMixin<T>
{
	private int oldListSize;

	@Inject(
			method = "scheduleTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/tick/ChunkTickScheduler;scheduleTick(Lnet/minecraft/world/tick/OrderedTick;)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void startScheduleTileTickEvent(OrderedTick<T> orderedTick, CallbackInfo ci, long l, ChunkTickScheduler<T> chunkTickScheduler)
	{
		this.oldListSize = chunkTickScheduler.getTickCount();
	}

	@Inject(
			method = "scheduleTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/tick/ChunkTickScheduler;scheduleTick(Lnet/minecraft/world/tick/OrderedTick;)V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void endScheduleTileTickEvent(OrderedTick<T> tt, CallbackInfo ci, long l, ChunkTickScheduler<T> chunkTickScheduler)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			int delay = (int)(tt.triggerTick() - WorldUtils.getWorldTime(serverWorld));
			MicroTimingLoggerManager.onScheduleTileTickEvent(serverWorld, tt.type(), tt.pos(), delay, tt.priority(), chunkTickScheduler.getTickCount() > this.oldListSize);
		}
	}
	
	// execute tile tick events, for mc1.18+

	@ModifyVariable(
			method = "tick(Ljava/util/function/BiConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					remap = false
			)
	)
	private OrderedTick<T> preExecuteBlockTileTickEvent(OrderedTick<T> event)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_START);
		}
		return event;
	}

	@ModifyVariable(
			method = "tick(Ljava/util/function/BiConsumer;)V",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/function/BiConsumer;accept(Ljava/lang/Object;Ljava/lang/Object;)V",
					shift = At.Shift.AFTER,
					remap = false
			)
	)
	private OrderedTick<T> postExecuteBlockTileTickEvent(OrderedTick<T> event)
	{
		ServerWorld serverWorld = ((ITileTickListWithServerWorld)this).getServerWorld();
		if (serverWorld != null)
		{
			MicroTimingLoggerManager.onExecuteTileTickEvent(serverWorld, event, EventType.ACTION_END);
		}
		return event;
	}
}
