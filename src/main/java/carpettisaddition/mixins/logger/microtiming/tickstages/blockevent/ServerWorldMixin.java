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

package carpettisaddition.mixins.logger.microtiming.tickstages.blockevent;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.BlockEventSubStage;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Shadow
	@Final
	private ObjectLinkedOpenHashSet<BlockAction> pendingBlockActions;

	private int blockEventOrderCounter;
	private int blockEventDepth;
	private int blockEventCurrentDepthCounter;
	private int blockEventCurrentDepthSize;

	@Inject(
			method = "sendBlockActions",
			at = @At("HEAD")
	)
	private void enterBlockEventStage_updateTickPhase(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.BLOCK_EVENT);
		this.blockEventOrderCounter = 0;
		this.blockEventCurrentDepthCounter = 0;
		this.blockEventDepth = 0;
		this.blockEventCurrentDepthSize = this.pendingBlockActions.size();
	}

	@Inject(
			method = "sendBlockActions",
			at = @At("TAIL")
	)
	private void exitBlockEventStage_updateTickPhase(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.UNKNOWN);
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "processBlockEvent",
			//#else
			method = "method_14174",
			//#endif
			at = @At("HEAD")
	)
	private void beforeBlockEventExecuted_updateTickPhase(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, String.valueOf(this.blockEventDepth));
		MicroTimingLoggerManager.setSubTickStage((ServerWorld)(Object)this, new BlockEventSubStage((ServerWorld)(Object)this, blockAction, this.blockEventOrderCounter++, this.blockEventDepth));
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "processBlockEvent",
			//#else
			method = "method_14174",
			//#endif
			at = @At("RETURN")
	)
	private void afterBlockEventExecuted_updateTickPhase(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, null);
		MicroTimingLoggerManager.setSubTickStage((ServerWorld)(Object)this, null);
		this.blockEventCurrentDepthCounter++;
		if (this.blockEventCurrentDepthCounter == this.blockEventCurrentDepthSize)
		{
			this.blockEventDepth++;
			this.blockEventCurrentDepthSize = this.pendingBlockActions.size();
			this.blockEventCurrentDepthCounter = 0;
		}
	}
}
