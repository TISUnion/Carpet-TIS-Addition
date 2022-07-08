package carpettisaddition.mixins.utils.settings;

import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.lang.reflect.Field;

@Mixin(ParsedRule.class)
public interface ParsedRuleAccessor
{
	@SuppressWarnings("rawtypes")
	@Invoker(value = "<init>", remap = false)
	static ParsedRule invokeConstructor(
			Field field, Rule rule
			//#if MC >= 11600
			//$$ , carpet.settings.SettingsManager settingsManager
			//#endif
	)
	{
		throw new RuntimeException();
	}
}
