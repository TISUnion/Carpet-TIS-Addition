package carpettisaddition.translations;

import carpet.utils.Messenger;
import carpet.utils.Translations;
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
	// - convert key to lowercase
	// - (optional) use String.trim() to remove leading and trailing spaces
	// - (optional) replace space with underscore
	private String getPath(String key, boolean autoFormat)
	{
		String path = this.getTranslationPath();
		if (!path.isEmpty())
		{
			path += ".";
		}
		key = key.toLowerCase();
		if (autoFormat)
		{
			key = key.trim().replace(" ", "_");
		}
		return path + key;
	}

	/**
	 * Full control mode
	 * @param key translation key
	 * @param text fallback text
	 * @param autoFormat if autoFormat the key will be auto format, including trimming and space replacement with _
	 * @return translated text
	 */
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

	public BaseText advTr(String key, String defaultKeyText, Object ...args)
	{
		String msgKeyString = this.tr(key, defaultKeyText);
		return Messenger.s(msgKeyString);  // TODO
	}
}
