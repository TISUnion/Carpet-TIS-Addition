package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.EventType;
import net.minecraft.block.Block;
import net.minecraft.server.world.BlockAction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
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

	@Inject(method = "addBlockAction", at = @At("HEAD"))
	private void onScheduleBlockEvent(BlockPos pos, Block block, int type, int data, CallbackInfo ci)
	{
		MicroTickLoggerManager.onScheduleBlockEvent((ServerWorld)(Object)this, new BlockAction(pos, block, type, data));
	}

	@Inject(method = "method_14174", at = @At(value = "HEAD", shift = At.Shift.AFTER))
	private void beforeBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTickLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, null, EventType.ACTION_START);
	}

	@Inject(method = "method_14174", at = @At("RETURN"))
	private void afterBlockEventExecuted(BlockAction blockAction, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTickLoggerManager.onExecuteBlockEvent((ServerWorld)(Object)this, blockAction, cir.getReturnValue(), EventType.ACTION_END);
	}
}
