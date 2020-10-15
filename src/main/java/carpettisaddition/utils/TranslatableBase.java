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

	private String getPath(String key, boolean autoConvert)
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
		if (autoConvert)
		{
			key = key.replace(" ", "_");
		}
		return path + key;
	}

	public String tr(String key, String text)
	{
		return Translations.tr(getPath(key, false), text);
	}

	public String tr(String key)
	{
		return Translations.tr(getPath(key, true), key);
	}
}
