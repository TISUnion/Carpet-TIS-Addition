package carpettisaddition.mixins.logger.microtiming.events.blockevent;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.events.ExecuteBlockEventEvent;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * Block Event
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Shadow @Final private ObjectLinkedOpenHashSet<BlockAction> pendingBlockActions;

	private int oldBlockActionQueueSize;

	@Inject(method = "addBlockAction", at = @At("HEAD"))
	private void startScheduleBlockEvent(BlockPos pos, Block block, int type, int data, CallbackInfo ci)
	{
		this.oldBlockActionQueueSize = this.pendingBlockActions.size();
	}

	@Inject(method = "addBlockAction", at = @At("RETURN"))
	private void endScheduleBlockEvent(BlockPos pos, Block block, int type, int data, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onScheduleBlockEvent((ServerWorld)(Object)this, new BlockAction(pos, block, type, data), this.pendingBlockActions.size() > this.oldBlockActionQueueSize);
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "processBlockEvent",
			//#else
			method = "method_14174",
			//#endif
			at = @At(
					value = "HEAD",
					shift = At.Shift.AFTER
			)
	)
	private void beforeBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, null, null, EventType.ACTION_START);
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "processBlockEvent",
			//#else
			method = "method_14174",
			//#endif
			at = @At("RETURN"),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void afterBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir, BlockState blockState)
	{
		ExecuteBlockEventEvent.FailInfo failInfo = new ExecuteBlockEventEvent.FailInfo(blockState.getBlock() != blockAction.getBlock() ? ExecuteBlockEventEvent.FailReason.BLOCK_CHANGED : ExecuteBlockEventEvent.FailReason.EVENT_FAIL, blockState.getBlock());
		MicroTimingLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, cir.getReturnValue(), failInfo, EventType.ACTION_END);
	}
}
