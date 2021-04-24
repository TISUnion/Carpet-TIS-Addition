package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import carpettisaddition.translations.TranslatableBase;

public abstract class AbstractReason extends TranslatableBase implements ToTextAble
{
	public AbstractReason(String reasonType)
	{
		super(LifeTimeTracker.getInstance().getTranslator().getDerivedTranslator(reasonType));
	}
}
