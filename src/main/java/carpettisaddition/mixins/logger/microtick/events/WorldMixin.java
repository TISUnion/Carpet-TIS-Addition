package carpettisaddition.mixins.logger.microtick.events;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(World.class)
public class WorldMixin
{
	@Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", at = @At("HEAD"))
	void startSetBlockState(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTickLoggerManager.onSetBlockState((World)(Object)this, pos, state, null, EventType.ACTION_START);
	}

	@Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", at = @At("RETURN"))
	void endSetBlockState(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<Boolean> cir)
	{
		MicroTickLoggerManager.onSetBlockState((World)(Object)this, pos, state, cir.getReturnValue(), EventType.ACTION_END);
	}
}
