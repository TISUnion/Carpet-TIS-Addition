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

package carpettisaddition.mixins.logger.microtiming.events.blockevent;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.events.ExecuteBlockEventEvent;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.BlockEventData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Block Event
 */
@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin
{
	@Shadow @Final private ObjectLinkedOpenHashSet<BlockEventData> blockEvents;

	private int oldBlockActionQueueSize;

	@Inject(method = "blockEvent", at = @At("HEAD"))
	private void startScheduleBlockEvent_storeEvent(BlockPos pos, Block block, int type, int data, CallbackInfo ci)
	{
		this.oldBlockActionQueueSize = this.blockEvents.size();
	}

	@Inject(method = "blockEvent", at = @At("RETURN"))
	private void endScheduleBlockEvent_storeEvent(BlockPos pos, Block block, int type, int data, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onScheduleBlockEvent((ServerLevel)(Object)this, new BlockEventData(pos, block, type, data), this.blockEvents.size() > this.oldBlockActionQueueSize);
	}

	@Inject(
			method = "doBlockEvent",
			at = @At(
					value = "HEAD",
					shift = At.Shift.AFTER
			)
	)
	private void beforeBlockEventExecuted_storeEvent(BlockEventData blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.onExecuteBlockEvent((ServerLevel)(Object)this, blockAction, null, null, EventType.ACTION_START);
	}

	@ModifyExpressionValue(
			method = "doBlockEvent",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
			)
	)
	private BlockState recordBlockState(BlockState blockState, @Share("currentBlock") LocalRef<BlockState> currentBlock)
	{
		currentBlock.set(blockState);
		return blockState;
	}

	@ModifyExpressionValue(
			method = "doBlockEvent",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;triggerEvent(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;II)Z"
			)
	)
	private boolean recordOnBlockActionReturnValue(boolean ret, @Share("retStore") LocalRef<Boolean> retStore)
	{
		retStore.set(ret);
		return ret;
	}

	@Inject(
			method = "doBlockEvent",
			at = @At("RETURN")
	)
	private void afterBlockEventExecuted_storeEvent(
			BlockEventData blockAction, CallbackInfoReturnable<Boolean> cir,
			@Share("retStore") LocalRef<Boolean> retStore, @Share("currentBlock") LocalRef<BlockState> currentBlock
	)
	{
		ExecuteBlockEventEvent.FailInfo failInfo;
		Boolean ret = retStore.get();
		if (ret == null)
		{
			// not invoked onBlockAction() at all, due to block change
			failInfo = new ExecuteBlockEventEvent.FailInfo(ExecuteBlockEventEvent.FailReason.BLOCK_CHANGED, currentBlock.get().getBlock());
		}
		else if (ret)
		{
			// onBlockAction() returns true
			failInfo = null;
		}
		else
		{
			// onBlockAction() returns false
			failInfo = new ExecuteBlockEventEvent.FailInfo(ExecuteBlockEventEvent.FailReason.EVENT_FAIL, currentBlock.get().getBlock());
		}

		MicroTimingLoggerManager.onExecuteBlockEvent((ServerLevel)(Object)this, blockAction, cir.getReturnValue(), failInfo, EventType.ACTION_END);
	}
}
