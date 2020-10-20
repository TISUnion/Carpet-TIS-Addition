package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.EventType;
import carpettisaddition.logging.loggers.microtick.events.ExecuteBlockEventEvent;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.BlockEvent;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;


@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	// Shift the opcode to make sure the stage detail and extra has been set in tickstages.ServerWorldMixin
	// Injects with shift below are the same
	@Inject(method = "tickBlock", at = @At(value = "HEAD", shift = At.Shift.AFTER))
	private void beforeExecuteTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTickLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_START);
	}

	@Inject(method = "tickBlock", at = @At("RETURN"))
	private void afterExecuteTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTickLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_END);
	}

	/*
	 * -------------
	 *  Block Event
	 * -------------
	 */

	@Shadow @Final private ObjectLinkedOpenHashSet<BlockEvent> syncedBlockEventQueue;
	private final ThreadLocal<Integer> oldBlockActionQueueSize = ThreadLocal.withInitial(() -> 0);

	@Inject(method = "addSyncedBlockEvent", at = @At("HEAD"))
	private void startScheduleBlockEvent(BlockPos pos, Block block, int type, int data, CallbackInfo ci)
	{
		oldBlockActionQueueSize.set(this.syncedBlockEventQueue.size());
	}

	@Inject(method = "addSyncedBlockEvent", at = @At("RETURN"))
	private void endScheduleBlockEvent(BlockPos pos, Block block, int type, int data, CallbackInfo ci)
	{
		MicroTickLoggerManager.onScheduleBlockEvent((ServerWorld)(Object)this, new BlockEvent(pos, block, type, data), this.syncedBlockEventQueue.size() > this.oldBlockActionQueueSize.get());
	}

	@Inject(method = "processBlockEvent", at = @At(value = "HEAD", shift = At.Shift.AFTER))
	private void beforeBlockEventExecuted(BlockEvent blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTickLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, null, null, EventType.ACTION_START);
	}

	@Inject(method = "processBlockEvent", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void afterBlockEventExecuted(BlockEvent blockAction, CallbackInfoReturnable<Boolean> cir, BlockState blockState)
	{
		MicroTickLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, cir.getReturnValue(), new ExecuteBlockEventEvent.FailInfo(blockState.getBlock() != blockAction.getBlock() ? ExecuteBlockEventEvent.FailReason.BLOCK_CHANGED : ExecuteBlockEventEvent.FailReason.EVENT_FAIL, blockState.getBlock()), EventType.ACTION_END);
	}
}
