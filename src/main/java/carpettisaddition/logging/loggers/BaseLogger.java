package carpettisaddition.logging.loggers;

import carpettisaddition.utils.TranslatableBase;

public abstract class BaseLogger extends TranslatableBase
{
	protected final static String MULTI_OPTION_SEP_REG = "[,. ]";

	public BaseLogger(String name)
	{
		super("logger", name);
	}
}
