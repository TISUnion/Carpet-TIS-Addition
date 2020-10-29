package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Inject(method = "postProcessState", at = @At("HEAD"))
	private static void startStateUpdate(BlockState state, WorldAccess worldAccess, BlockPos pos, CallbackInfoReturnable<BlockState> cir)
	{
		if (worldAccess instanceof World)
		{
			MicroTimingLoggerManager.onBlockUpdate((World)worldAccess, pos, worldAccess.getBlockState(pos).getBlock(), BlockUpdateType.STATE_UPDATE, null, EventType.ACTION_START);
		}
	}

	@Inject(method = "postProcessState", at = @At("RETURN"))
	private static void endStateUpdate(BlockState state, WorldAccess worldAccess, BlockPos pos, CallbackInfoReturnable<BlockState> cir)
	{
		if (worldAccess instanceof World)
		{
			MicroTimingLoggerManager.onBlockUpdate((World)worldAccess, pos, worldAccess.getBlockState(pos).getBlock(), BlockUpdateType.STATE_UPDATE, null, EventType.ACTION_END);
		}
	}
}
