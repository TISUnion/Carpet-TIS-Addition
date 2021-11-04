package carpettisaddition.translations;

import carpettisaddition.utils.Messenger;
import net.minecraft.text.BaseText;

public class Translator implements Translatable
{
	private final String type;
	private final String name;
	private final String translationPath;

	public Translator(String type, String name)
	{
		this.type = type;
		this.name = name;
		this.translationPath = this.generateTranslationPath();
	}

	public Translator(String prefix)
	{
		this(prefix, null);
	}

	public Translator getDerivedTranslator(String derivedName)
	{
		String name = this.name == null ? derivedName : this.name + "." + derivedName;
		return new Translator(this.type, name);
	}

	public String getTranslationPath()
	{
		return this.translationPath;
	}

	private String generateTranslationPath()
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
		if (path.endsWith("."))
		{
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	// use <type>.<name>.<key> as translation key
	// format key before apply
	private String getPath(String key)
	{
		String path = this.getTranslationPath();
		if (!path.isEmpty())
		{
			path += ".";
		}
		key = key.toLowerCase();
		return path + key;
	}

	@Override
	public BaseText tr(String key, Object... args)
	{
		return Messenger.tr(this.getPath(key), args);
	}
}
