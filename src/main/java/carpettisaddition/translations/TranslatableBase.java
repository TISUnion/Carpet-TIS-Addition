package carpettisaddition.translations;

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
	public String tr(String key, String text, boolean autoFormat)
	{
		return this.translator.tr(key, text, autoFormat);
	}

	@Override
	public String tr(String key, String text)
	{
		return this.translator.tr(key, text);
	}

	@Override
	public String tr(String key)
	{
		return this.translator.tr(key);
	}
}
