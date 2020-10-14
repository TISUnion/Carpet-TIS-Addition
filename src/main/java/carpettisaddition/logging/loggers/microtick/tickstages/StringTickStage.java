package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.ToTextAble;
import net.minecraft.text.Text;

public class StringTickStage implements ToTextAble
{
	private final String info;

	public StringTickStage(String info)
	{
		this.info = info;
	}

	@Override
	public Text toText()
	{
		return Messenger.s(this.info);
	}
}
