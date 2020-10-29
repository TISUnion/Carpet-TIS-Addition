package carpettisaddition.logging.loggers.microtiming.tickstages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import net.minecraft.text.ClickEvent;

public abstract class TickStageExtraBase implements ToTextAble
{
	public String tr(String key, String text)
	{
		return MicroTimingLoggerManager.tr("stage_extra." + key, text);
	}

	public String tr(String text)
	{
		return MicroTimingLoggerManager.tr("stage_extra." + text, text, true);
	}

	public ClickEvent getClickEvent()
	{
		return null;
	}
}
