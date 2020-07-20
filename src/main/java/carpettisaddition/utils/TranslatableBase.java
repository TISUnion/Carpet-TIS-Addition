package carpettisaddition.utils;


public abstract class TranslatableBase
{
	protected String type;
	protected String name;

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
		if (autoConvert)
		{
			key = key.toLowerCase().replace(" ", "_");
		}
		return path + key;
	}

	public String tr(String key, String text)
	{
		return text;
	}

	public String tr(String key)
	{
		return key;
	}
}
