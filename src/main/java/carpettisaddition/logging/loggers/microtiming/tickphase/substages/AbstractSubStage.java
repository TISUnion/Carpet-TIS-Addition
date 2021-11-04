package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.translations.TranslatableBase;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

public abstract class AbstractSubStage extends TranslatableBase
{
	public AbstractSubStage()
	{
		super(MicroTimingLoggerManager.TRANSLATOR.getDerivedTranslator("sub_stage"));
	}

	public ClickEvent getClickEvent()
	{
		return null;
	}

	public abstract BaseText toText();
}
