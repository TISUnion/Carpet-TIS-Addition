package carpettisaddition.mixins.rule.tntIgnoreRedstoneSignal;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.GameUtil;
import net.minecraft.block.TntBlock;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin
{
	/**
	 * Carpet rule tntDoNotUpdate already applied @Redirect
	 * So here comes the @ModifyArg
	 */
	@ModifyArg(
			method = {
					"onBlockAdded",
					"neighborUpdate"
			},
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;isReceivingRedstonePower(Lnet/minecraft/util/math/BlockPos;)Z"
			)
	)
	private BlockPos tntIgnoreRedstoneSignalImpl(BlockPos pos)
	{
		if (CarpetTISAdditionSettings.tntIgnoreRedstoneSignal)
		{
			return GameUtil.getInvalidBlockPos();
		}
		return pos;
	}
}
