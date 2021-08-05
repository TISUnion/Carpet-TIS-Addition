package carpettisaddition.mixins.rule.totallyNoBlockUpdate;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.RedstoneWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin
{
	// method to update wire connection
	@Inject(method = "method_9517", at = @At("HEAD"), cancellable = true)
	private void disableStateUpdateMaybe(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.totallyNoBlockUpdate)
		{
			ci.cancel();
		}
	}
}
