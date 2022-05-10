package carpettisaddition.utils;

public class StringUtil
{
	public static String removePrefix(String string, String... prefixes)
	{
		for (String prefix : prefixes)
		{
			if (string.startsWith(prefix))
			{
				string = string.substring(prefix.length());
			}
		}
		return string;
	}

	public static String removeSuffix(String string, String... suffixes)
	{
		for (String suffix : suffixes)
		{
			if (string.endsWith(suffix))
			{
				string = string.substring(0, string.length() - suffix.length());
			}
		}
		return string;
	}
}
