package carpettisaddition.mixins.rule.repeaterHalfDelay;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.AbstractRandom;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin
{
	@Shadow protected abstract int getUpdateDelayInternal(BlockState state);

	@Redirect(
			method = "scheduledTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/AbstractRedstoneGateBlock;getUpdateDelayInternal(Lnet/minecraft/block/BlockState;)I"
			)
	)
	private int modifyRepeaterDelay(AbstractRedstoneGateBlock abstractRedstoneGateBlock, BlockState state1, BlockState state2, ServerWorld world, BlockPos pos, AbstractRandom random)
	{
		return this.getModifiedDelay(abstractRedstoneGateBlock, world, pos, state1);
	}

	@Redirect(
			method = "updatePowered",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/AbstractRedstoneGateBlock;getUpdateDelayInternal(Lnet/minecraft/block/BlockState;)I"
			)
	)
	private int modifyRepeaterDelay(AbstractRedstoneGateBlock abstractRedstoneGateBlock, BlockState state1, World world, BlockPos pos, BlockState state2)
	{
		return this.getModifiedDelay(abstractRedstoneGateBlock, world, pos, state1);
	}

	private int getModifiedDelay(AbstractRedstoneGateBlock abstractRedstoneGateBlock, World world, BlockPos pos, BlockState state)
	{
		int delay = this.getUpdateDelayInternal(state);
		if (CarpetTISAdditionSettings.repeaterHalfDelay)
		{
			if (abstractRedstoneGateBlock instanceof RepeaterBlock && world.getBlockState(pos.down()).getBlock() == Blocks.REDSTONE_ORE)
			{
				delay /= 2;
			}
		}
		return delay;
	}
}
