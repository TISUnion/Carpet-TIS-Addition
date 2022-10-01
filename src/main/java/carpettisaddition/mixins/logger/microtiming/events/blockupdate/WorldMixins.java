package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11900
//$$ import net.minecraft.block.BlockState;
//#endif

@SuppressWarnings("ConstantConditions")
public abstract class WorldMixins
{
	@Mixin(
			//#if MC >= 11900
			//$$ ServerWorld.class
			//#else
			World.class
			//#endif
	)
	public static class BlockUpdateMixin
	{
		@Inject(method = "updateNeighborsAlways", at = @At("HEAD"))
		private void startUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate((World) (Object) this, pos, block, BlockUpdateType.BLOCK_UPDATE, null);
			}
		}

		@Inject(method = "updateNeighborsAlways", at = @At("RETURN"))
		private void endUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END);
			}
		}
	}

	@Mixin(
			//#if MC >= 11900
			//$$ ServerWorld.class
			//#else
			World.class
			//#endif
	)
	public static class BlockUpdateExceptMixin
	{
		@Inject(method = "updateNeighborsExcept", at = @At("HEAD"))
		private void startUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_START);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate((World) (Object) this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction);
			}
		}

		@Inject(method = "updateNeighborsExcept", at = @At("RETURN"))
		private void endUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_END);
			}
		}
	}

	@Mixin(World.class)
	public static class ComparatorUpdateMixin
	{
		@Inject(
				//#if MC >= 11600
				//$$ method = "updateComparators",
				//#else
				method = "updateHorizontalAdjacent",
				//#endif
				at = @At("HEAD")
		)
		private void startUpdateComparator(BlockPos pos, Block block, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_START);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate((World) (Object) this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null);
			}
		}

		@Inject(
				//#if MC >= 11600
				//$$ method = "updateComparators",
				//#else
				method = "updateHorizontalAdjacent",
				//#endif
				at = @At("RETURN")
		)
		private void endUpdateComparator(BlockPos pos, Block block, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_END);
			}
		}
	}

	@Mixin(
			//#if MC >= 11900
			//$$ ServerWorld.class
			//#else
			World.class
			//#endif
	)
	public static class SingleBlockUpdateMixin
	{
		@Inject(method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
		private void startUpdateSingleBlock(BlockPos sourcePos, Block sourceBlock, BlockPos neighborPos, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, sourcePos, sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate((World) (Object) this, sourcePos, sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null);
			}
		}

		@Inject(method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", at = @At("RETURN"))
		private void endUpdateSingleBlock(BlockPos sourcePos, Block sourceBlock, BlockPos neighborPos, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World) (Object) this))
			{
				MicroTimingLoggerManager.onBlockUpdate((World) (Object) this, sourcePos, sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
			}
		}
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(ServerWorld.class)
	public static class SingleBlockUpdate2Mixin
	{
		//#if MC >= 11900
		//$$ @Inject(method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At("HEAD"))
		//$$ private void startUpdateSingleBlock2(BlockState blockState, BlockPos blockPos, Block block, BlockPos sourceBlock, boolean bl, CallbackInfo ci)
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, blockPos, block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
		//$$ 	}
		//$$ 	else
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, blockPos, block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null);
		//$$ 	}
		//$$ }
		//$$
		//$$ @Inject(method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At("TAIL"))
		//$$ private void endUpdateSingleBlock2(BlockState blockState, BlockPos blockPos, Block block, BlockPos sourceBlock, boolean bl, CallbackInfo ci)
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, blockPos, block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
		//$$ 	}
		//$$ }
		//#endif
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(World.class)
	public static class SingleStateUpdateMixin
	{
		//#if MC >= 11900
		//$$ @Inject(method = "replaceWithStateForNeighborUpdate", at = @At("HEAD"))
		//$$ private void startStateUpdateSingleBlock(Direction direction, BlockState blockState, BlockPos blockPos, BlockPos sourcePos, int flags, int maxUpdateDepth, CallbackInfo ci)
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, sourcePos, blockState.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_START);
		//$$ 	}
		//$$ 	else
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, sourcePos, blockState.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null);
		//$$ 	}
		//$$ }
		//$$ @Inject(method = "replaceWithStateForNeighborUpdate", at = @At("HEAD"))
		//$$ private void endStateUpdateSingleBlock(Direction direction, BlockState blockState, BlockPos blockPos, BlockPos sourcePos, int flags, int maxUpdateDepth, CallbackInfo ci)
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, sourcePos, blockState.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_END);
		//$$ 	}
		//$$ }
		//#endif
	}
}
