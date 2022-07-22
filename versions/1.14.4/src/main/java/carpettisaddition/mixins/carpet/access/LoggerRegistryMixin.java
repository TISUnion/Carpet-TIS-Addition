package carpettisaddition.mixins.carpet.access;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.compat.IExtensionLogger;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
/**
 * for extension logging supports in 1.14.4
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.15"))
@Mixin(LoggerRegistry.class)
public abstract class LoggerRegistryMixin
{
	private static Logger currentProcessingLogger;

	@Inject(method = "setAccess", at = @At("HEAD"), remap = false)
	private static void youCanUseMyField(Logger logger, CallbackInfo ci)
	{
		currentProcessingLogger = logger;
	}

	@Redirect(
			method = "setAccess",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Class;getDeclaredField(Ljava/lang/String;)Ljava/lang/reflect/Field;"
			),
			remap = false
	)
	private static Field maybeDontUseYourOwnGetDeclaredField(Class<?> aClass, String name) throws NoSuchFieldException
	{
		if (currentProcessingLogger instanceof IExtensionLogger)
		{
			return ((IExtensionLogger)currentProcessingLogger).getAcceleratorField();
		}
		return aClass.getDeclaredField(name);
	}
}