package carpettisaddition.logging.logHelpers;

import carpet.utils.Translations;

public abstract class AbstractLoggerHelper
{
	protected static String loggerName = null;

	protected static String tr(String key, String text)
	{
		return Translations.tr(String.format("logger.%s.%s", loggerName, key), text);
	}

	protected static String tr(String key)
	{
		return Translations.tr(String.format("logger.%s.%s", loggerName, key), key);
	}
}
