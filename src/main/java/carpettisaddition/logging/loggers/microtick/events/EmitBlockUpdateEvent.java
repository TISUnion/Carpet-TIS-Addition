package carpettisaddition.logging.loggers.microtick.events;

import carpettisaddition.logging.loggers.microtick.types.EventType;
import net.minecraft.text.Text;

public class EmitBlockUpdateEvent extends BaseEvent
{
	protected EmitBlockUpdateEvent(EventType eventType)
	{
		super(eventType, "emit_block_update");
	}

	@Override
	public Text toText()
	{
		return null;
	}

}
