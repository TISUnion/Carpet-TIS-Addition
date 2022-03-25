package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class ChainRestrictedNeighborUpdaterMixins
{
	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$SimpleEntry")
	public static class SimpleEntryMixin
	{
		@Shadow @Final private Block sourceBlock;
		@Shadow @Final private BlockPos pos;

		@Inject(method = "update", at = @At("HEAD"), remap = false)
		private void startBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "update", at = @At("TAIL"), remap = false)
		private void endBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
		}
	}
	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$StatefulEntry")
	public static class StatefulEntryMixin
	{
		@Shadow @Final private Block sourceBlock;
		@Shadow @Final private BlockPos pos;

		@Inject(method = "update", at = @At("HEAD"), remap = false)
		private void startBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "update", at = @At("TAIL"), remap = false)
		private void endBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
		}
	}

	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$SixWayEntry")
	public static class SixWayEntryMixin
	{
		@Shadow @Final private Block sourceBlock;
		@Shadow @Final private BlockPos pos;
		@Shadow @Final private Direction except;

		private boolean hasTriggeredStartEvent$CTA = false;

		@Inject(method = "update", at = @At("HEAD"), remap = false)
		private void startBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			if (!this.hasTriggeredStartEvent$CTA)
			{
				this.hasTriggeredStartEvent$CTA = true;
				if (this.except == null)
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START);
				}
				else
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, this.except, EventType.ACTION_START);
				}
			}
		}

		@Inject(method = "update", at = @At("TAIL"), remap = false)
		private void endBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			if (!cir.getReturnValue())  // it's finished, returning false
			{
				if (this.except == null)
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END);
				}
				else
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, this.except, EventType.ACTION_END);
				}
			}
		}
	}

	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$class_7272")
	public static class StateUpdateEntryMixin
	{
		@Shadow(remap = false) @Final private BlockState state;
		@Shadow(remap = false) @Final private BlockPos neighborPos;

		@Inject(method = "update", at = @At("HEAD"), remap = false)
		private void startStateUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.neighborPos, this.state.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "update", at = @At("TAIL"), remap = false)
		private void endStateUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.neighborPos, this.state.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_END);
		}
	}
}
