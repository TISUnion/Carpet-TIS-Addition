package carpettisaddition.mixins.settings;

//#if MC >= 11901
//$$ import carpet.api.settings.InvalidRuleValueException;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

import carpet.settings.Validator;
import carpettisaddition.settings.validator.AbstractValidator;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

//#if MC >= 11901
//$$ @SuppressWarnings({"deprecation", "removal"})
//#endif
@Mixin(
		// dont remap to CarpetRule
		//#disable-remap
		carpet.settings.ParsedRule.class
		//#enable-remap
)
public class ParsedRuleMixin<T>
{
	@Inject(
			//#if MC >= 11901
			//$$ method = "set(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/Object;Ljava/lang/String;)V",
			//#else
			method = "set(Lnet/minecraft/server/command/ServerCommandSource;Ljava/lang/Object;Ljava/lang/String;)Lcarpet/settings/ParsedRule;",
			//#endif
			at = @At(
					value = "INVOKE_ASSIGN",
					//#if MC >= 11901
					//$$ target = "Lcarpet/api/settings/Validator;validate(Lnet/minecraft/server/command/ServerCommandSource;Lcarpet/api/settings/CarpetRule;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;",
					//#else
					target = "Lcarpet/settings/Validator;validate(Lnet/minecraft/server/command/ServerCommandSource;Lcarpet/settings/ParsedRule;Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;",
					//#endif
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
			//#if MC < 11901
			, cancellable = true
			//#endif
	)
	private void TISCMValidatorLogic(
			ServerCommandSource source, T value, String stringValue,
			//#if MC >= 11901
			//$$ CallbackInfo ci,
			//#else
			CallbackInfoReturnable<?> cir,
			//#endif
			Iterator<?> iterator, Validator<T> validator
	)
	//#if MC >= 11901
	//$$ throws InvalidRuleValueException
	//#endif
	{
		if (value == null)
		{
			if (validator instanceof AbstractValidator)
			{
				//#if MC >= 11901
				//$$ throw new InvalidRuleValueException();
				//#else
				cir.setReturnValue(null);
				//#endif
			}
		}
	}
}
