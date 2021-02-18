package carpettisaddition.mixins.rule.fluidDestructionDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.fluid.FlowableFluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FlowableFluid.class)
public class FlowableFluidMixin
{
	@Inject(
			method = "flow",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/fluid/FlowableFluid;beforeBreakingBlock(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
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
