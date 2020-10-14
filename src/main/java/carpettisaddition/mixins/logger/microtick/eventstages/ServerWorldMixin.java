package carpettisaddition.mixins.logger.microtick.eventstages;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.tickstages.BlockEventTickStageExtra;
import carpettisaddition.logging.loggers.microtick.tickstages.TileTickTickStageExtra;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ScheduledTick;
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
	/*
	 * -----------
	 *  Tile Tick
	 * -----------
	 */

	private int tileTickOrderCounter = 0;

	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=tickPending"
			)
	)
	private void onEnterTileTickStage(CallbackInfo ci)
	{
		this.tileTickOrderCounter = 0;
	}

	@Inject(method = "tickBlock", at = @At("HEAD"))
	private void beforeExecuteTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStageDetail((ServerWorld)(Object)this, String.valueOf(event.priority.getIndex()));
		MicroTickLoggerManager.setTickStageExtra((ServerWorld)(Object)this, new TileTickTickStageExtra(event, this.tileTickOrderCounter++));
		MicroTickLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_START);
	}

	@Inject(method = "tickBlock", at = @At("TAIL"))
	private void afterExecuteTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStageDetail((ServerWorld)(Object)this, null);
		MicroTickLoggerManager.setTickStageExtra((ServerWorld)(Object)this, null);
		MicroTickLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_END);
	}

	/*
	 * -------------
	 *  Block Event
	 * -------------
	 */

	@Shadow
	@Final
	private ObjectLinkedOpenHashSet<BlockAction> pendingBlockActions;

	@Shadow protected abstract boolean method_14174(BlockAction blockAction);

	private int blockEventOrderCounter;
	private int blockEventDepth;
	private int blockEventCurrentDepthCounter;
	private int blockEventCurrentDepthSize;

	@Inject(
			method = "sendBlockActions",
			at = @At("HEAD")
	)
	private void onEnterBlockEventStage(CallbackInfo ci)
	{
		this.blockEventOrderCounter = 0;
		this.blockEventCurrentDepthCounter = 0;
		this.blockEventDepth = 0;
		this.blockEventCurrentDepthSize = this.pendingBlockActions.size();
	}

	@Inject(method = "method_14174", at = @At("HEAD"))
	private void beforeBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTickLoggerManager.setTickStageDetail((ServerWorld)(Object)this, String.valueOf(this.blockEventDepth));
		MicroTickLoggerManager.setTickStageExtra((ServerWorld)(Object)this, new BlockEventTickStageExtra(blockAction, this.blockEventOrderCounter++, this.blockEventDepth));
		MicroTickLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, null, EventType.ACTION_START);
	}

	@Inject(method = "method_14174", at = @At("RETURN"))
	private void afterBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTickLoggerManager.setTickStageDetail((ServerWorld)(Object)this, null);
		MicroTickLoggerManager.setTickStageExtra((ServerWorld)(Object)this, null);
		MicroTickLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, cir.getReturnValue(), EventType.ACTION_END);
		this.blockEventCurrentDepthCounter++;
		if (this.blockEventCurrentDepthCounter == this.blockEventCurrentDepthSize)
		{
			this.blockEventDepth++;
			this.blockEventCurrentDepthSize = this.pendingBlockActions.size();
			this.blockEventCurrentDepthCounter = 0;
		}
	}
}
