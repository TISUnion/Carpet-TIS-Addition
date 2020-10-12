package carpettisaddition.logging.loggers.microtick.tickstages;

import carpet.utils.Messenger;
import net.minecraft.text.Text;

public class StringTickStage implements TickStage
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
