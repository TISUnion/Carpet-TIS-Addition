package carpettisaddition.utils;

import carpet.utils.Translations;

public class TranslatableBase
{
	private final String type;
	private final String name;

	public TranslatableBase(String type, String name)
	{
		this.type = type;
		this.name = name;
	}

	public TranslatableBase(String prefix)
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

	public String tr(String key, String text, boolean autoFormat)
	{
		return Translations.tr(getPath(key, autoFormat), text);
	}

	public String tr(String key, String text)
	{
		return tr(key, text, false);
	}

	public String tr(String key)
	{
		return Translations.tr(getPath(key, true), key);
	}
}
