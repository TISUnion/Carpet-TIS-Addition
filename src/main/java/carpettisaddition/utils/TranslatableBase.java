package carpettisaddition.utils;

import carpet.utils.Translations;


public abstract class TranslatableBase
{
	protected String type;
	protected String name;

	public TranslatableBase(String type, String name)
	{
		this.type = type;
		this.name = name;
	}

	private String getPath(String key)
	{
		return String.format("%s.%s.%s", type, name, key.toLowerCase());
	}

	protected String tr(String key, String text)
	{
		return Translations.tr(getPath(key), text);
	}

	protected String tr(String key)
	{
		return tr(key, key);
	}
}
