package carpettisaddition.mixins.logger.microtick.eventstages;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.types.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.types.MessageType;
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
	@Inject(method = "updateNeighborStates", at = @At("HEAD"))
	private void startStateUpdate(BlockState state, IWorld world, BlockPos pos, int flags, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate(world.getWorld(), pos, world.getBlockState(pos).getBlock(), BlockUpdateType.POST_PLACEMENT, null, MessageType.ACTION_START);
	}

	@Inject(method = "updateNeighborStates", at = @At("TAIL"))
	private void endStateUpdate(BlockState state, IWorld world, BlockPos pos, int flags, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate(world.getWorld(), pos, world.getBlockState(pos).getBlock(), BlockUpdateType.POST_PLACEMENT, null, MessageType.ACTION_END);
	}
}
