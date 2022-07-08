package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.Deque;


@Mixin(World.class)
public abstract class WorldMixin
{
	@Shadow public abstract BlockState getBlockState(BlockPos pos);

	/*
	 * --------------------------
	 *  BlockState Change starts
	 * --------------------------
	 */

	private final ThreadLocal<Deque<BlockState>> previousBlockState = ThreadLocal.withInitial(ArrayDeque::new);

	@Inject(
			//#if MC >= 11600
			//$$ method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
			//#else
			method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
			//#endif
			at = @At("HEAD")
	)
	private void startSetBlockState(
			BlockPos pos, BlockState newState, int flags,
			//#if MC >= 11600
			//$$ int maxUpdateDepth,
			//#endif
			CallbackInfoReturnable<Boolean> cir
	)
	{
		if (MicroTimingLoggerManager.isLoggerActivated())
		{
			BlockState oldState = this.getBlockState(pos);
			this.previousBlockState.get().push(oldState);
			MicroTimingLoggerManager.onSetBlockState((World)(Object)this, pos, oldState, newState, null, flags, EventType.ACTION_START);
		}
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
			//#else
			method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
			//#endif
			at = @At("RETURN")
	)
	private void endSetBlockState(
			BlockPos pos, BlockState newState, int flags,
			//#if MC >= 11600
			//$$ int maxUpdateDepth,
			//#endif
			CallbackInfoReturnable<Boolean> cir
	)
	{
		if (MicroTimingLoggerManager.isLoggerActivated() && !this.previousBlockState.get().isEmpty())
		{
			BlockState oldState = this.previousBlockState.get().pop();
			MicroTimingLoggerManager.onSetBlockState((World)(Object)this, pos, oldState, newState, cir.getReturnValue(), flags, EventType.ACTION_END);
		}
	}

	// To avoid leaking memory after update suppression or whatever thing
	@Inject(
			//#if MC >= 11600
			//$$ method = "tickBlockEntities",
			//#else
			method = "tickTime",
			//#endif
			at = @At("HEAD")
	)
	private void cleanStack(CallbackInfo ci)
	{
		this.previousBlockState.get().clear();
	}

	/*
	 * ------------------------
	 *  BlockState Change ends
	 * ------------------------
	 */

	@Inject(method = "updateNeighborsAlways", at = @At("HEAD"))
	private void startUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START);
	}
	@Inject(method = "updateNeighborsAlways", at = @At("RETURN"))

	private void endUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END);
	}

	@Inject(method = "updateNeighborsExcept", at = @At("HEAD"))
	private void startUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_START);
	}

	@Inject(method = "updateNeighborsExcept", at = @At("RETURN"))
	private void endUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_END);
	}

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
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_START);
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
		MicroTimingLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_END);
	}

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
}
