package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.Block;
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

@Mixin(World.class)
public abstract class WorldMixin
{
	@Inject(method = "updateNeighborsAlways", at = @At("HEAD"))
	private void startUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		//#if MC >= 11900
		//$$ MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.BLOCK_UPDATE, null);
		//#else
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START);
		//#endif
	}

	//#if MC < 11900
	@Inject(method = "updateNeighborsAlways", at = @At("RETURN"))
	private void endUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END);
	}
	//#endif

	@Inject(method = "updateNeighborsExcept", at = @At("HEAD"))
	private void startUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		//#if MC >= 11900
		//$$ MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction);
		//#else
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_START);
		//#endif
	}

	//#if MC < 11900
	@Inject(method = "updateNeighborsExcept", at = @At("RETURN"))
	private void endUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_END);
	}
	//#endif

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
		//#if MC >= 11900
		//$$ MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null);
		//#else
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_START);
		//#endif
	}

	//#if MC < 11900
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
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_END);
	}
	//#endif

	//#if MC >= 11900
	//$$ @Inject(method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
	//$$ private void startUpdateSingleBlock(BlockPos pos, Block sourceBlock, BlockPos neighborPos, CallbackInfo ci)
	//$$ {
	//$$ 	MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null);
	//$$ }
 //$$
	//$$ @Inject(method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At("HEAD"))
	//$$ private void startUpdateSingleBlock(BlockState blockState, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl, CallbackInfo ci)
	//$$ {
	//$$ 	MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, blockPos, block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null);
	//$$ }
 //$$
	//$$ @Inject(method = "replaceWithStateForNeighborUpdate", at = @At("HEAD"))
	//$$ private void startScheduleStateUpdate(Direction direction, BlockState blockState, BlockPos blockPos, BlockPos sourcePos, int flags, int maxUpdateDepth, CallbackInfo ci)
	//$$ {
	//$$ 	MicroTimingLoggerManager.onScheduleBlockUpdate((World)(Object)this, sourcePos, blockState.getBlock(), BlockUpdateType.SINGLE_STATE_UPDATE, null);
	//$$ }
 //$$
	//#else

	@Inject(method = "updateNeighbor", at = @At("HEAD"))
	private void startUpdateSingleBlock(BlockPos pos, Block sourceBlock, BlockPos neighborPos, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
	}

	@Inject(method = "updateNeighbor", at = @At("HEAD"))
	private void endUpdateSingleBlock(BlockPos pos, Block sourceBlock, BlockPos neighborPos, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
	}
	//#endif
}
