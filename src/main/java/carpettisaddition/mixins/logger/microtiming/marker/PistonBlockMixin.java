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

//#if MC >= 11500
import java.util.Map;
//#else
//$$ import java.util.Set;
//#endif

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin
{
	@SuppressWarnings("rawtypes")
	@Inject(
			method = "move",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11500
							target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"
							//#else
							//$$ target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/world/World;addBlockEntity(Lnet/minecraft/block/entity/BlockEntity;)V",
					//#else
					target = "Lnet/minecraft/world/World;setBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;)V",
					//#endif
					shift = At.Shift.AFTER,
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void moveMicroTimingMarkerAsWell(
			World world, BlockPos pos, Direction dir, boolean retract,
			CallbackInfoReturnable<Boolean> cir,
			//#if MC >= 11600
			//$$ BlockPos blockPos, PistonHandler pistonHandler, Map map, List list, List list2, List list3, BlockState[] blockStates, Direction direction, int j, int l, BlockPos blockPos4, BlockState blockState3
			//#elseif MC >= 11500
			BlockPos blockPos, PistonHandler pistonHandler, Map map, List list, List list2, List list3, int j, BlockState[] blockStates, Direction direction, int l, BlockPos blockPos4, BlockState blockState3
			//#else
			//$$ BlockPos blockPos, PistonHandler pistonHandler, List list, List list2, List list3, int j, BlockState[] blockStates, Direction direction, Set set, int l, BlockPos blockPos4, BlockState blockState2
			//#endif
	)
	{
		MicroTimingLoggerManager.moveMarker(world, blockPos4.offset(direction.getOpposite()), direction);
	}
}
