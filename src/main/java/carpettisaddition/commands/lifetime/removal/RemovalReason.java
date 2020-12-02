package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import carpettisaddition.translations.TranslatableBase;

public abstract class RemovalReason extends TranslatableBase implements ToTextAble
{
	public RemovalReason()
	{
		super(LifeTimeTracker.getInstance().getTranslator().getTranslationPath(), "removal_reason");
	}
}
