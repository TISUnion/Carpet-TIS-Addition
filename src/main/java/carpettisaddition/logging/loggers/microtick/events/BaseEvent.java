package carpettisaddition.logging.loggers.microtick.events;

import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import carpettisaddition.utils.TranslatableBase;

import java.util.Objects;

public abstract class BaseEvent extends TranslatableBase implements ToTextAble
{
	private final EventType eventType;

	protected BaseEvent(EventType eventType, String translateKey)
	{
		super("logger.microtick.event", translateKey);
		this.eventType = eventType;
	}

	public EventType getEventType()
	{
		return this.eventType;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof BaseEvent)) return false;
		BaseEvent baseEvent = (BaseEvent) o;
		return eventType == baseEvent.eventType;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(eventType);
	}
}
