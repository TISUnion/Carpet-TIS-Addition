package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.ToTextAble;
import carpettisaddition.translations.TranslatableBase;
import net.minecraft.block.Block;

import java.util.Objects;

public abstract class BaseEvent extends TranslatableBase implements ToTextAble
{
	private final Block eventSourceBlock;

	protected static final String COLOR_ACTION = "c ";
	protected static final String COLOR_TARGET = "c ";
	protected static final String COLOR_RESULT = "q ";

	private EventType eventType;

	protected BaseEvent(EventType eventType, String translateKey, Block eventSourceBlock)
	{
		super("logger.microTiming.event", translateKey);
		this.eventType = eventType;
		this.eventSourceBlock = eventSourceBlock;
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
		if (o == null || getClass() != o.getClass()) return false;
		BaseEvent baseEvent = (BaseEvent) o;
		return Objects.equals(eventSourceBlock, baseEvent.eventSourceBlock) && eventType == baseEvent.eventType;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(eventSourceBlock, eventType);
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

	public Block getEventSourceBlock()
	{
		return this.eventSourceBlock;
	}
}
