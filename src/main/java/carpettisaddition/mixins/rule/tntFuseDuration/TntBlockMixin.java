package carpettisaddition.mixins.rule.tntFuseDuration;

import net.minecraft.block.TntBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin
{
	@ModifyArg(
			method = "onDestroyedByExplosion",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11900
					//$$ target = "Lnet/minecraft/util/math/random/Random;nextInt(I)I"
					//#else
					target = "Ljava/util/Random;nextInt(I)I",
					remap = false
					//#endif
			),
			index = 0
	)
	private int makeSureItIsPositive(int bound)
	{
		return Math.max(bound, 1);
	}
}
