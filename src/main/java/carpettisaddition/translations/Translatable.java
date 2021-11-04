package carpettisaddition.translations;

import net.minecraft.text.BaseText;

public interface Translatable
{
	BaseText tr(String key, Object... args);
}
