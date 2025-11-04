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

package carpettisaddition.mixins.logger.lightqueue;

import carpettisaddition.logging.loggers.lightqueue.IServerLightingProvider;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicLong;

@Mixin(ThreadedLevelLightEngine.class)
public abstract class ServerLightingProviderMixin implements IServerLightingProvider
{
	@Unique private final AtomicLong enqueuedTaskCount = new AtomicLong();
	@Unique private final AtomicLong executedTaskCount = new AtomicLong();
	@Unique private final AtomicLong queueSize = new AtomicLong();

	@Inject(
			method = "addTask(IILjava/util/function/IntSupplier;Lnet/minecraft/server/level/ThreadedLevelLightEngine$TaskType;Ljava/lang/Runnable;)V",
			at = @At(value = "TAIL")
	)
	private void onEnqueuedLightUpdateTask(CallbackInfo ci)
	{
		this.enqueuedTaskCount.getAndIncrement();
		this.queueSize.getAndIncrement();
	}

	@Override
	public void onExecutedLightUpdates$TISCM()
	{
		this.executedTaskCount.getAndIncrement();
		this.queueSize.getAndDecrement();
	}

	@Inject(
			method = "runUpdate",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/objects/ObjectListIterator;remove()V",
					remap = false
			)
	)
	private void onExecutedLightUpdates(CallbackInfo ci)
	{
		this.onExecutedLightUpdates$TISCM();
	}

	@Override
	public long getEnqueuedTaskCountAndClean$TISCM()
	{
		return this.enqueuedTaskCount.getAndSet(0);
	}

	@Override
	public long getExecutedTaskCountAndClean$TISCM()
	{
		return this.executedTaskCount.getAndSet(0);
	}

	@Override
	public long getQueueSize$TISCM()
	{
		return this.queueSize.get();
	}
}
