package carpettisaddition.mixins.rule.fluidDestructionDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.fluid.BaseFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFluid.class)
public class BaseFluidMixin
{
	@Inject(
			method = "flow",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/fluid/BaseFluid;beforeBreakingBlock(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
			),
			cancellable = true
	)
	private void stopBreakingBlock(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.fluidDestructionDisabled)
		{
			ci.cancel();
		}
	}
}
