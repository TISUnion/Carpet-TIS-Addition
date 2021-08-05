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
			method = "initLoggers",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=projectiles",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/logging/Logger;<init>(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V",
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
