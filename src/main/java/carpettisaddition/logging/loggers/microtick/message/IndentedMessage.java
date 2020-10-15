package carpettisaddition.logging.loggers.microtick.message;

import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import net.minecraft.text.BaseText;

public class IndentedMessage implements ToTextAble
{
	private final MicroTickMessage message;
	private final int indentation;

	public IndentedMessage(MicroTickMessage message, int indentation)
	{
		this.message = message;
		this.indentation = indentation;
	}

	public MicroTickMessage getMessage()
	{
		return this.message;
	}

	@Override
	public BaseText toText()
	{
		return this.message.toText(this.indentation, this.indentation == 0);
	}
}
