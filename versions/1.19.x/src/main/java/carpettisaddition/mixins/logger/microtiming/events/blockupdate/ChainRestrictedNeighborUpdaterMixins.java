package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
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

/**
 * 1.19+ manual stack update chain logic, for block /state update event stuffs
 */
public abstract class ChainRestrictedNeighborUpdaterMixins
{
	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$SimpleEntry")
	public static class SimpleEntryMixin
	{
		@Shadow @Final private Block sourceBlock;
		@Shadow @Final private BlockPos pos;

		@Inject(method = "update", at = @At("HEAD"))
		private void startBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "update", at = @At("TAIL"))
		private void endBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
		}
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$StatefulEntry")
	public static class StatefulEntryMixin
	{
		@Shadow @Final private Block sourceBlock;
		@Shadow @Final private BlockPos pos;

		@Inject(method = "update", at = @At("HEAD"))
		private void startBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "update", at = @At("TAIL"))
		private void endBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
		}
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$SixWayEntry")
	public static class SixWayEntryMixin
	{
		@Shadow @Final private Block sourceBlock;
		@Shadow @Final private BlockPos pos;
		@Shadow @Final private Direction except;

		private boolean hasTriggeredStartEvent$TISCM = false;

		@Inject(method = "update", at = @At("HEAD"))
		private void startBlockUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			if (!this.hasTriggeredStartEvent$TISCM)
			{
				this.hasTriggeredStartEvent$TISCM = true;
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

		@Inject(method = "update", at = @At("TAIL"))
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

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.block.ChainRestrictedNeighborUpdater$StateReplacementEntry")
	public static class StateReplacementEntryMixin
	{
		@Shadow @Final private BlockState neighborState;
		@Shadow @Final private BlockPos neighborPos;

		@Inject(method = "update", at = @At("HEAD"))
		private void startStateUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.neighborPos, this.neighborState.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "update", at = @At("TAIL"))
		private void endStateUpdate(World world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.neighborPos, this.neighborState.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_END);
		}
	}
}