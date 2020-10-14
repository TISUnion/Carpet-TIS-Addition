package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import net.minecraft.text.BaseText;

public class StringTickStage implements ToTextAble
{
	private final String info;

	public StringTickStage(String info)
	{
		this.info = info;
	}

	@Override
	public BaseText toText()
	{
		return Messenger.s(this.info);
	}
}
