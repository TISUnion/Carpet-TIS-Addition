package carpettisaddition.logging.loggers.microtiming.message;

import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import net.minecraft.text.BaseText;

public class IndentedMessage implements ToTextAble
{
	private final MicroTimingMessage message;
	private final int indentation;

	public IndentedMessage(MicroTimingMessage message, int indentation)
	{
		this.message = message;
		this.indentation = indentation;
	}

	public MicroTimingMessage getMessage()
	{
		return this.message;
	}

	@Override
	public BaseText toText()
	{
		return this.message.toText(this.indentation, this.indentation == 0);
	}
}
