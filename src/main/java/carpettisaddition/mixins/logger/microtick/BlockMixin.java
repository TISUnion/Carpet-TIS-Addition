package carpettisaddition.mixins.logger.microtick;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.ActionRelation;
import carpettisaddition.logging.loggers.microtick.enums.BlockUpdateType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Inject(
			method = "updateNeighborStates",
			at = @At(value = "HEAD")
	)
	private void onStateUpdateStarts(BlockState state, IWorld world, BlockPos pos, int flags, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate(world.getWorld(), pos, world.getBlockState(pos).getBlock(), ActionRelation.PRE_ACTION, BlockUpdateType.POST_PLACEMENT);
	}

	@Inject(
			method = "updateNeighborStates",
			at = @At(value = "TAIL")
	)
	private void onStateUpdateEnds(BlockState state, IWorld world, BlockPos pos, int flags, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate(world.getWorld(), pos, world.getBlockState(pos).getBlock(), ActionRelation.POST_ACTION, BlockUpdateType.POST_PLACEMENT);
	}
}
