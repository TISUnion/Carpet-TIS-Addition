package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.EventType;
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


@Mixin(World.class)
public abstract class WorldMixin
{
	@Shadow public abstract BlockState getBlockState(BlockPos pos);

	private final ThreadLocal<BlockState> previousBlockState = new ThreadLocal<BlockState>();

	@Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", at = @At("HEAD"))
	void startSetBlockState(BlockPos pos, BlockState newState, int flags, CallbackInfoReturnable<Boolean> cir)
	{
		if (MicroTickLoggerManager.isLoggerActivated())
		{
			this.previousBlockState.set(this.getBlockState(pos));
			MicroTickLoggerManager.onSetBlockState((World)(Object)this, pos, this.previousBlockState.get(), newState, cir.getReturnValue(), EventType.ACTION_START);
		}
	}

	@Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", at = @At("RETURN"))
	void endSetBlockState(BlockPos pos, BlockState newState, int flags, CallbackInfoReturnable<Boolean> cir)
	{
		if (MicroTickLoggerManager.isLoggerActivated())
		{
			MicroTickLoggerManager.onSetBlockState((World)(Object)this, pos, this.previousBlockState.get(), newState, cir.getReturnValue(), EventType.ACTION_END);
		}
	}

	@Inject(method = "updateNeighborsAlways", at = @At("HEAD"))
	private void startUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START);
	}
	@Inject(method = "updateNeighborsAlways", at = @At("RETURN"))

	private void endUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END);
	}

	@Inject(method = "updateNeighborsExcept", at = @At("RETURN"))
	private void startUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_START);
	}

	@Inject(method = "updateNeighborsExcept", at = @At("TAIL"))
	private void endUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_END);
	}
}
