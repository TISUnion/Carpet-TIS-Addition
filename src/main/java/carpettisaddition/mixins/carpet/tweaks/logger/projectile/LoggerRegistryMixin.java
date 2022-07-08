package carpettisaddition.mixins.carpet.tweaks.logger.projectile;

import carpet.logging.LoggerRegistry;
import com.google.common.collect.Lists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

import java.util.List;

@Mixin(LoggerRegistry.class)
public abstract class LoggerRegistryMixin
{
	@ModifyArg(
			//#if MC >= 11500
			method = "registerLoggers",
			//#else
			//$$ method = "initLoggers",
			//#endif
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=projectiles",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lcarpet/logging/Logger;stardardLogger(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)Lcarpet/logging/Logger;",
					//#else
					//$$ target = "Lcarpet/logging/Logger;<init>(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V",
					//#endif
					ordinal = 0,
					remap = false
			),
			index = 2,
			remap = false
	)
	private static String[] modifyProjectileLoggerOptions(String[] options)
	{
		List<String> optionList = Lists.newArrayList(options);
		optionList.add("visualize");
		return optionList.toArray(new String[0]);
	}
}
