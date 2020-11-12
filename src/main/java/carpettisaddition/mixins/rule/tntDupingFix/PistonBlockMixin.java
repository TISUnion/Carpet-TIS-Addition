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
import java.util.Set;


@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin
{
	private final ThreadLocal<Boolean> isDupeFixed = ThreadLocal.withInitial(() -> false);

	/**
	 * Set all blocks to be moved to air without any kind of update first (yeeted attached block updater like dead coral),
	 * then let vanilla codes to set the air blocks into b36
	 * Before setting a block to air, store the block state right before setting it to air to make sure no block desync
	 * will happen (yeeted onRemoved block updater like lit observer).
	 */
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
					shift = At.Shift.AFTER,  // to make sure this will be injected after onMove in PistonBlock_movableTEMixin in fabric-carpet
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void setAllToBeMovedBlockToAirFirst(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler, List<BlockPos> list, List<BlockState> list2, List list3, int j, BlockState blockStates[], Direction direction, Set set)
	{
		// just in case the rule gets changed halfway
		this.isDupeFixed.set(CarpetTISAdditionSettings.tntDupingFix);

		if (this.isDupeFixed.get())
		{
			// vanilla iterating order
			for (int l = list.size() - 1; l >= 0; --l)
			{
				BlockPos toBeMovedBlockPos = list.get(l);
				// Get the current state to make sure it is the state we want
				BlockState toBeMovedBlockState = world.getBlockState(toBeMovedBlockPos);
				// Added 16 to the vanilla flag, resulting in no block update or state update
				// Although this cannot yeet onRemoved updaters, but it can prevent attached blocks from breaking,
				// which is nicer than just let them break imo
				world.setBlockState(toBeMovedBlockPos, Blocks.AIR.getDefaultState(), 68 + 16);

				// Update containers which contain the old state
				list2.set(l, toBeMovedBlockState);
				// map stores block pos and block state of moved blocks which changed into air due to block being moved
				// not in 1.14 tho
				// map.put(toBeMovedBlockPos, toBeMovedBlockState);
			}
		}
	}

	/**
	 * Just to make sure blockStates array contains the correct values
	 * But ..., when reading states from it, mojang itself inverts the order and reads the wrong state releative to the blockpos
	 * When assigning:
	 *   blockStates = (list concat with list3 in order).map(world::getBlockState)
	 * When reading:
	 *   match list3[list3.size()-1] with blockStates[0]
	 *   match list3[list3.size()-2] with blockStates[1]
	 *   ...
	 * The block pos matches wrongly with block state, so mojang uses the wrong block as the source block to emit block updates :thonk:
	 *
	 * Whatever, just make it behave like vanilla
	 */
	@Inject(
			method = "move",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							target = "Lnet/minecraft/block/PistonBlock;isSticky:Z"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;iterator()Ljava/util/Iterator;",
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void makeSureStatesInBlockStatesIsCorrect(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir, BlockPos blockPos, PistonHandler pistonHandler, List<BlockPos> list, List<BlockState> list2, List<BlockPos> list3, int j, BlockState[] blockStates)
	{
		if (this.isDupeFixed.get())
		{
			// since blockState8 = world.getBlockState(blockPos4) always return AIR due to the changes above
			// some states value in blockStates array need to be corrected
			// list and list2 has the same size and indicating the same block
			int j2 = j + list.size();
			for (int l2 = list.size() - 1; l2 >= 0; --l2)
			{
				--j2;
				blockStates[j2] = list2.get(l2);
			}
		}
	}
}
