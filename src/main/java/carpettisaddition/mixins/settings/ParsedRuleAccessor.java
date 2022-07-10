package carpettisaddition.mixins.settings;

import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11901
//$$ import carpettisaddition.utils.compat.DummyClass;
//#else
import carpet.settings.ParsedRule;
import carpet.settings.Rule;
import org.spongepowered.asm.mixin.gen.Invoker;
import java.lang.reflect.Field;
//#endif

@Mixin(
		//#if MC >= 11901
		//$$ DummyClass.class
		//#else
		ParsedRule.class
		//#endif
)
public interface ParsedRuleAccessor
{
	//#if MC < 11901
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
	//#endif
}
