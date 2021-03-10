package carpettisaddition.mixins.carpet.tntRandomRange;

import carpet.settings.ParsedRule;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "carpet.CarpetSettings$CheckOptimizedTntEnabledValidator")
public class CheckOptimizedTntEnabledValidatorMixin<T>
{
	@Inject(method = "validate", at = @At("HEAD"), cancellable = true, remap = false)
	private void stopCheckingOptimizedTntEnabled(ServerCommandSource source, ParsedRule<T> currentRule, T newValue, String string, CallbackInfoReturnable<T> cir)
	{
		cir.setReturnValue(newValue);
	}
}
