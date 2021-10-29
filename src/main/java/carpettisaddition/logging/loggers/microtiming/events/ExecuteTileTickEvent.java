package carpettisaddition.logging.loggers.microtiming.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.class_6760;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ExecuteTileTickEvent<T> extends BaseEvent
{
	private final class_6760<T> tileTickEntry;

	private ExecuteTileTickEvent(EventType eventType, class_6760<T> tileTickEntry, EventSource source)
	{
		super(eventType, "execute_tile_tick", source);
		this.tileTickEntry = tileTickEntry;
	}

	public static Optional<ExecuteTileTickEvent<?>> createFrom(EventType eventType, class_6760<?> tileTickEntry)
	{
		return EventSource.fromObject(tileTickEntry.type()).map(eventSource -> new ExecuteTileTickEvent<>(eventType, tileTickEntry, eventSource));
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(COLOR_ACTION + this.tr("Execute"));
		list.add(TextUtil.getSpaceText());
		list.add(COLOR_TARGET + this.tr("TileTick Event"));
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add(TextUtil.getSpaceText());
			list.add(COLOR_RESULT + MicroTimingLoggerManager.tr("ended"));
		}
		list.add(String.format("^w %s: %d (%s)", MicroTimingLoggerManager.tr("Priority"), this.tileTickEntry.priority().getIndex(), this.tileTickEntry.priority()));
		return Messenger.c(list.toArray(new Object[0]));
	}


	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ExecuteTileTickEvent)) return false;
		if (!super.equals(o)) return false;
		ExecuteTileTickEvent<?> that = (ExecuteTileTickEvent<?>) o;
		return Objects.equals(tileTickEntry, that.tileTickEntry);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), tileTickEntry);
	}
}
