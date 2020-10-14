package carpettisaddition.logging.loggers.microtick.events;

import carpettisaddition.logging.loggers.microtick.ToTextAble;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.utils.TranslatableBase;
import net.minecraft.world.World;

public abstract class BaseEvent extends TranslatableBase implements ToTextAble
{
	protected final World world;
	protected final EventType eventType;

	protected BaseEvent(World world, EventType eventType, String translateKey)
	{
		super("microtick.event", translateKey);
		this.eventType = eventType;
		this.world = world;
	}

	public EventType getEventType()
	{
		return this.eventType;
	}
}
