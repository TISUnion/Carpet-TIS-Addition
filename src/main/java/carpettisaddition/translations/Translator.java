package carpettisaddition.translations;

import carpettisaddition.utils.Messenger;
import com.google.common.base.Strings;
import net.minecraft.text.BaseText;

public class Translator
{
	private final String translationPath;

	public Translator(String translationPath)
	{
		if (Strings.isNullOrEmpty(translationPath) || translationPath.startsWith(".") || translationPath.endsWith("."))
		{
			throw new RuntimeException("Invalid translation path: " + translationPath);
		}
		this.translationPath = translationPath;
	}

	public Translator getDerivedTranslator(String derivedName)
	{
		return new Translator(this.translationPath + "." + derivedName);
	}

	public String getTranslationPath()
	{
		return this.translationPath;
	}

	public BaseText tr(String key, Object... args)
	{
		String translationKey = TISAdditionTranslations.TRANSLATION_KEY_PREFIX + this.translationPath + "." + key;
		return Messenger.tr(translationKey, args);
	}
}
