package carpettisaddition.mixins.logger.microtiming.events.blockstatechange;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayDeque;
import java.util.Deque;

/*
 * BlockState Change starts
 */
@Mixin(World.class)
public abstract class WorldMixin
{
	@Shadow public abstract BlockState getBlockState(BlockPos pos);

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
}
