package carpettisaddition.logging.loggers.microtiming.tickphase.substages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

public abstract class AbstractSubStage
{
	public String tr(String key, String text)
	{
		return MicroTimingLoggerManager.tr("sub_stage." + key, text);
	}

	public String tr(String text)
	{
		return MicroTimingLoggerManager.tr("sub_stage." + text, text, true);
	}

	public ClickEvent getClickEvent()
	{
		return null;
	}

	public abstract BaseText toText();
}
