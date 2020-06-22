package carpettisaddition.mixins;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;


@Mixin(PistonBlock.class)
public abstract class PistonBlock_tntDupingFixMixin
{
	@Inject(
			method = "move",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/block/Block;hasBlockEntity()Z"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;size()I",
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void setAllToBeMovedBlockToAirFirst(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler, Map<BlockPos, BlockState> map, List<BlockPos> list, List<BlockState> list2, List<BlockPos> list3, BlockState blockStates[], Direction direction, int j)
	{
		if (CarpetTISAdditionSettings.tntDupingFix)
		{
			for(int l = list.size() - 1; l >= 0; --l) {
				BlockPos toBeMovedBlockPos = list.get(l);
				list2.set(l, world.getBlockState(toBeMovedBlockPos));
				world.setBlockState(toBeMovedBlockPos, Blocks.AIR.getDefaultState(), 18);
			}
		}
	}

	@Inject(
			method = "move",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;",
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void useTheStateInList2Please(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler, Map<BlockPos, BlockState> map, List<BlockPos> list, List<BlockState> list2, List<BlockPos> list3, BlockState blockStates[], Direction direction, int j, int l, BlockPos blockPos4, BlockState blockState3)
	{
		if (CarpetTISAdditionSettings.tntDupingFix)
		{
			blockState3 = list2.get(l);
		}
	}
}
