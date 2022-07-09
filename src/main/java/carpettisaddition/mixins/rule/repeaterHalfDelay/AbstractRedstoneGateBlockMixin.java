package carpettisaddition.mixins.rule.repeaterHalfDelay;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

//#if MC >= 11900
//$$ import net.minecraft.util.math.random.Random;
//#else
import java.util.Random;
//#endif

//#if MC >= 11500
import net.minecraft.server.world.ServerWorld;
//#endif

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin
{
	@Shadow protected abstract int getUpdateDelayInternal(BlockState state);

	@Redirect(
			//#if MC >= 11500
			method = "scheduledTick",
			//#else
			//$$ method = "onScheduledTick",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/AbstractRedstoneGateBlock;getUpdateDelayInternal(Lnet/minecraft/block/BlockState;)I"
			)
	)
	private int modifyRepeaterDelay(
			AbstractRedstoneGateBlock abstractRedstoneGateBlock, BlockState state1, BlockState state2,
			//#if MC >= 11500
			ServerWorld world,
			//#else
			//$$ World world,
			//#endif
			BlockPos pos, Random random
	)
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
