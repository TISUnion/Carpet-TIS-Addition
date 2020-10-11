package carpettisaddition.mixins.rule.redstoneDustRandomUpdateOrder;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;
import java.util.Random;


@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin
{
	private final Random random = new Random();

	@Inject(
			method = "update",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Set;clear()V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void letsMakeTheOrderUnpredictable(World world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir, List<BlockPos> list)
	{
		if (CarpetTISAdditionSettings.redstoneDustRandomUpdateOrder)
		{
			Collections.shuffle(list, random);
		}
	}
}
