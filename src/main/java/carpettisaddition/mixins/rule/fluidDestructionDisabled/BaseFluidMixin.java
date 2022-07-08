package carpettisaddition.mixins.rule.fluidDestructionDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11600
//$$ import net.minecraft.fluid.FlowableFluid;
//#else
import net.minecraft.fluid.BaseFluid;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ FlowableFluid.class
		//#else
		BaseFluid.class
		//#endif
)
public class BaseFluidMixin
{
	@Inject(
			method = "flow",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/fluid/FlowableFluid;beforeBreakingBlock(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
					//#else
					target = "Lnet/minecraft/fluid/BaseFluid;beforeBreakingBlock(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
					//#endif
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
