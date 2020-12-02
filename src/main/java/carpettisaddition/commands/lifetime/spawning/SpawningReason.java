package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import carpettisaddition.translations.TranslatableBase;

public abstract class SpawningReason extends TranslatableBase implements ToTextAble
{
	public SpawningReason()
	{
		super(LifeTimeTracker.getInstance().getTranslator().getTranslationPath(), "spawn_reason");
	}
}
