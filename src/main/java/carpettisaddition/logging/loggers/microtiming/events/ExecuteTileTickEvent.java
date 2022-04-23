package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.text.MutableText;
import net.minecraft.world.tick.OrderedTick;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ExecuteTileTickEvent<T> extends BaseEvent
{
	private final OrderedTick<T> tileTickEntry;

	private ExecuteTileTickEvent(EventType eventType, OrderedTick<T> tileTickEntry, EventSource source)
	{
		super(eventType, "execute_tile_tick", source);
		this.tileTickEntry = tileTickEntry;
	}

	public static Optional<ExecuteTileTickEvent<?>> createFrom(EventType eventType, OrderedTick<?> tileTickEntry)
	{
		return EventSource.fromObject(tileTickEntry.type()).map(eventSource -> new ExecuteTileTickEvent<>(eventType, tileTickEntry, eventSource));
	}

	@Override
	public MutableText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("execute"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		list.add(Messenger.fancy(
				Messenger.formatting(tr("tiletick_event"), COLOR_TARGET),
				Messenger.c(
						tr("priority"),
						String.format("w : %d (%s)", this.tileTickEntry.priority().getIndex(), this.tileTickEntry.priority())
				),
				null
		));
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add(Messenger.getSpaceText());
			list.add(Messenger.formatting(tr("ended"), COLOR_RESULT));
		}
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
