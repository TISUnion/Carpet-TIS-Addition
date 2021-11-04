package carpettisaddition.translations;

import net.minecraft.text.BaseText;

public class TranslatableBase implements Translatable
{
	private final Translator translator;

	public TranslatableBase(Translator translator)
	{
		this.translator = translator;
	}

	public TranslatableBase(String type, String name)
	{
		this(new Translator(type, name));
	}

	public Translator getTranslator()
	{
		return translator;
	}

	@Override
	public BaseText tr(String key, Object ...args)
	{
		return this.translator.tr(key, args);
	}
}
