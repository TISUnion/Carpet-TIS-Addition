package carpettisaddition.logging.loggers;

import carpettisaddition.translations.TranslationContext;

public abstract class AbstractLogger extends TranslationContext
{
	protected final static String MULTI_OPTION_SEP_REG = "[,. ]";

	public AbstractLogger(String name)
	{
		super("logger." + name);
	}
}
