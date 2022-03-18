package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class ScheduledBlockUpdaterMixins
{
	@Mixin(targets = {
			"net.minecraft.class_7159$class_7160",
			"net.minecraft.class_7159$class_7163",
	})
	public static class SimpleUpdaterMixin
	{
		@Shadow(remap = false) @Final private Block block;
		@Shadow(remap = false) @Final private BlockPos pos;

		@Inject(method = "method_41707", at = @At("HEAD"), remap = false)
		private void startBlockUpdate(ServerWorld serverWorld, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(serverWorld, this.pos, this.block, BlockUpdateType.SINGLE_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "method_41707", at = @At("TAIL"), remap = false)
		private void endBlockUpdate(ServerWorld serverWorld, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(serverWorld, this.pos, this.block, BlockUpdateType.SINGLE_UPDATE, null, EventType.ACTION_END);
		}
	}

	@Mixin(targets = "net.minecraft.class_7159$class_7161")
	public static class MultiUpdaterMixin
	{
		@Shadow(remap = false) @Final private Block field_37835;
		@Shadow(remap = false) @Final private BlockPos field_37834;
		@Shadow(remap = false) @Final private Direction field_37836;

		private boolean hasTriggeredStartEvent$CTA = false;

		@Inject(method = "method_41707", at = @At("HEAD"), remap = false)
		private void startBlockUpdate(ServerWorld serverWorld, CallbackInfoReturnable<Boolean> cir)
		{
			if (!this.hasTriggeredStartEvent$CTA)
			{
				this.hasTriggeredStartEvent$CTA = true;
				if (this.field_37836 == null)
				{
					MicroTimingLoggerManager.onBlockUpdate(serverWorld, this.field_37834, this.field_37835, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START);
				}
				else
				{
					MicroTimingLoggerManager.onBlockUpdate(serverWorld, this.field_37834, this.field_37835, BlockUpdateType.BLOCK_UPDATE_EXCEPT, this.field_37836, EventType.ACTION_START);
				}
			}
		}

		@Inject(method = "method_41707", at = @At("TAIL"), remap = false)
		private void endBlockUpdate(ServerWorld serverWorld, CallbackInfoReturnable<Boolean> cir)
		{
			if (!cir.getReturnValue())  // it's finished, returning false
			{
				if (this.field_37836 == null)
				{
					MicroTimingLoggerManager.onBlockUpdate(serverWorld, this.field_37834, this.field_37835, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END);
				}
				else
				{
					MicroTimingLoggerManager.onBlockUpdate(serverWorld, this.field_37834, this.field_37835, BlockUpdateType.BLOCK_UPDATE_EXCEPT, this.field_37836, EventType.ACTION_END);
				}
			}
		}
	}
}
