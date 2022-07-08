package carpettisaddition.utils.compat.carpet;

/**
 * A Fake one for mc1.14.4
 */
public class Translations
{
	public static String tr(String key)
	{
		return tr(key, key);
	}

	public static String tr(String key, String fallback)
	{
		return fallback;
	}
}
