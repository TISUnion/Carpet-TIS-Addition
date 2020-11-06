package carpettisaddition.translations;

import carpet.utils.Translations;

public class Translator implements Translatable
{
	private final String type;
	private final String name;

	public Translator(String type, String name)
	{
		this.type = type;
		this.name = name;
	}

	public Translator(String prefix)
	{
		this(null, prefix);
	}

	// use <type>.<name>.<key> as translation key
	// format key before apply
	// - convert key to lowercase
	// - (optional) use String.trim() to remove leading and trailing spaces
	// - (optional) replace space with underscore
	private String getPath(String key, boolean autoFormat)
	{
		String path = "";
		if (this.type != null)
		{
			path += this.type + ".";
		}
		if (this.name != null)
		{
			path += this.name + ".";
		}
		key = key.toLowerCase();
		if (autoFormat)
		{
			key = key.trim().replace(" ", "_");
		}
		return path + key;
	}

	@Override
	public String tr(String key, String text, boolean autoFormat)
	{
		return Translations.tr(getPath(key, autoFormat), text);
	}

	@Override
	public String tr(String key, String text)
	{
		return tr(key, text, false);
	}

	@Override
	public String tr(String key)
	{
		return Translations.tr(getPath(key, true), key);
	}
}
