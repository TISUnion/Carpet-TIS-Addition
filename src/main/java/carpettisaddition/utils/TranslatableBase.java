package carpettisaddition.utils;

import carpet.utils.Translations;


public abstract class TranslatableBase
{
	private final String type;
	private final String name;

	public TranslatableBase(String type, String name)
	{
		this.type = type;
		this.name = name;
	}

	// use <type>.<name>.<key> as translation key
	// format key before apply
	// - convert key to lowercase
	// - (optional) use String.trim() to remove leading and trailing spaces
	// - (optional) replace space with underscore
	private String getPath(String key, boolean autoFormat)
	{
		String path = "";
		if (type != null)
		{
			path += type + ".";
		}
		if (name != null)
		{
			path += name + ".";
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
