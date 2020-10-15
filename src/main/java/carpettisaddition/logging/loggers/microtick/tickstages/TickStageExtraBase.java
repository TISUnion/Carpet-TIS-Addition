package carpettisaddition.logging.loggers.microtick.tickstages;

import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import net.minecraft.text.ClickEvent;

public abstract class TickStageExtraBase implements ToTextAble
{
	public ClickEvent getClickEvent()
	{
		return null;
	}
}
