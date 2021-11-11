package carpettisaddition.translations;

import net.minecraft.text.BaseText;

/**
 * With this you can use {@link TranslationContext#tr} freely in your target class
 */
public class TranslationContext
{
	private final Translator translator;

	public TranslationContext(Translator translator)
	{
		this.translator = translator;
	}

	public TranslationContext(String translationPath)
	{
		this(new Translator(translationPath));
	}

	public Translator getTranslator()
	{
		return translator;
	}

	public BaseText tr(String key, Object ...args)
	{
		return this.translator.tr(key, args);
	}
}
