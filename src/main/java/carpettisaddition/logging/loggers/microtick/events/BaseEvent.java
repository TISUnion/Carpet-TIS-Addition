package carpettisaddition.logging.loggers.microtick.events;

import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.ToTextAble;
import carpettisaddition.utils.TranslatableBase;

import java.util.Objects;

public abstract class BaseEvent extends TranslatableBase implements ToTextAble
{
	private EventType eventType;

	protected BaseEvent(EventType eventType, String translateKey)
	{
		super("logger.microtick.event", translateKey);
		this.eventType = eventType;
	}

	// if it's not important, it can be ignore if it's on a leaf node
	public boolean isImportant()
	{
		return true;
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

	protected EventType getMergedEventType(BaseEvent quitEvent)
	{
		if (this.eventType == EventType.ACTION_START && quitEvent.eventType == EventType.ACTION_END)
		{
			return EventType.ACTION;
		}
		else
		{
			return this.eventType;
		}
	}

	public void mergeQuitEvent(BaseEvent quitEvent)
	{
		this.eventType = this.getMergedEventType(quitEvent);
	}
}
