package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.translations.TranslationContext;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.Objects;

public abstract class BaseEvent extends TranslationContext
{
	protected static final String COLOR_ACTION = "c";
	protected static final String COLOR_TARGET = "c";
	protected static final String COLOR_RESULT = "q";

	private final EventSource eventSource;
	private EventType eventType;

	protected BaseEvent(EventType eventType, String translateKey, EventSource eventSource)
	{
		super(MicroTimingLoggerManager.TRANSLATOR.getTranslationPath() + ".event." + translateKey);
		this.eventType = eventType;
		this.eventSource = eventSource;
	}

	protected BaseEvent(EventType eventType, String translateKey, Block eventSourceBlock)
	{
		this(eventType, translateKey, new EventSource.Block(eventSourceBlock));
	}

	public abstract BaseText toText();

	// if it's not important, it can be ignored if it's on a leaf node
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
		return Objects.equals(eventSource, baseEvent.eventSource) && eventType == baseEvent.eventType;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(eventSource, eventType);
	}

	public void mergeQuitEvent(BaseEvent quitEvent)
	{
		if (this.eventType == EventType.ACTION_START && quitEvent.eventType == EventType.ACTION_END)
		{
			this.eventType = EventType.ACTION;
		}
	}

	public EventSource getEventSource()
	{
		return this.eventSource;
	}
}
