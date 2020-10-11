package carpettisaddition.mixins.rule.tntDupingFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import java.util.Set;


@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin
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
					shift = At.Shift.AFTER,  // to make sure this will beinject after onMove in PistonBlock_movableTEMixin in fabric-carpet
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void setAllToBeMovedBlockToAirFirst(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler, List<BlockPos> list, List<BlockState> list2, List list3, int j, BlockState blockStates[], Direction direction, Set set)
	{
		if (CarpetTISAdditionSettings.tntDupingFix)
		{
			for(int l = list.size() - 1; l >= 0; --l)
			{
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
					target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z",
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void useTheStateInList2Please(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler, List list, List<BlockState> list2, List list3, int j, BlockState blockStates[], Direction direction, Set set, int l, BlockPos blockPos4, BlockState blockState2)
	{
		if (CarpetTISAdditionSettings.tntDupingFix)
		{
			blockState2 = list2.get(l);
		}
	}
}
