package carpettisaddition.logging.loggers;

import carpettisaddition.utils.TranslatableBase;

public abstract class TranslatableLogger extends TranslatableBase
{
	public TranslatableLogger(String name)
	{
		super("logger", name);
	}
}
