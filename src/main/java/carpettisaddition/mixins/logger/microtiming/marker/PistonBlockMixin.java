package carpettisaddition.mixins.logger.microtiming.marker;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin
{
	@SuppressWarnings("rawtypes")
	@Inject(
			method = "move",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;addBlockEntity(Lnet/minecraft/block/entity/BlockEntity;)V",
					shift = At.Shift.AFTER,
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void moveMicroTimingMarkerAsWell(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler, Map map, List list, List list2, List list3, BlockState[] blockStates, Direction direction, int j, int l, BlockPos blockPos4, BlockState blockState3)
	{
		MicroTimingLoggerManager.moveMarker(world, blockPos4.offset(direction.getOpposite()), direction);
	}
}
