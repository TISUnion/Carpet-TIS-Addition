package carpettisaddition.mixins.settings;

import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(SettingsManager.class)
public interface SettingsManagerAccessor
{
	@Accessor(value = "rules", remap = false)
	Map<String, ParsedRule<?>> getRules$TISCM();
}
