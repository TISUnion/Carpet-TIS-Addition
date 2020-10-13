package carpettisaddition.mixins.logger.microtick.eventstages;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtick.enums.MessageType;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin
{
	@Inject(
			method = "updateNeighborsAlways",
			at = @At("HEAD")
	)
	private void startUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.NEIGHBOR_CHANGED, null, MessageType.ACTION_START);
	}
	@Inject(
			method = "updateNeighborsAlways",
			at = @At("TAIL")
	)

	private void endUpdateNeighborsAlways(BlockPos pos, Block block, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, block, BlockUpdateType.NEIGHBOR_CHANGED, null, MessageType.ACTION_END);
	}

	@Inject(
			method = "updateNeighborsExcept",
			at = @At("HEAD")
	)
	private void startUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.NEIGHBOR_CHANGED_EXCEPT, direction, MessageType.ACTION_START);
	}

	@Inject(
			method = "updateNeighborsExcept",
			at = @At("TAIL")
	)
	private void endUpdateNeighborsExcept(BlockPos pos, Block sourceBlock, Direction direction, CallbackInfo ci)
	{
		MicroTickLoggerManager.onBlockUpdate((World)(Object)this, pos, sourceBlock, BlockUpdateType.NEIGHBOR_CHANGED_EXCEPT, direction, MessageType.ACTION_END);
	}
}
