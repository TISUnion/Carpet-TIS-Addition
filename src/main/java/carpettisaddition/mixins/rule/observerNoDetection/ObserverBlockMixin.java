package carpettisaddition.mixins.rule.observerNoDetection;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.ObserverBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ObserverBlock.class)
public abstract class ObserverBlockMixin
{
	@Inject(method = "scheduleTick", at = @At("HEAD"), cancellable = true)
	private void observerDisabled(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.observerNoDetection)
		{
			ci.cancel();
		}
	}
}
