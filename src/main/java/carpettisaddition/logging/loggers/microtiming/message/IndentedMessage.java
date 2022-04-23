package carpettisaddition.logging.loggers.microtiming.message;

import net.minecraft.text.MutableText;

public class IndentedMessage
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

	public int getIndentation()
	{
		return indentation;
	}

	public MutableText toText()
	{
		return this.message.toText(this.indentation, this.indentation == 0);
	}
}
