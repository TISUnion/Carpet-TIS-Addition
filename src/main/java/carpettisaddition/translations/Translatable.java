package carpettisaddition.translations;

import net.minecraft.text.BaseText;

public interface Translatable
{
	String tr(String key, String text, boolean autoFormat);

	String tr(String key, String text);

	String tr(String key);

	BaseText advTr(String key, String defaultKeyText, Object... args);
}
