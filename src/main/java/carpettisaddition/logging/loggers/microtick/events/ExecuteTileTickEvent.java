package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import net.minecraft.world.ScheduledTick;

import java.util.List;
import java.util.Objects;

public class ExecuteTileTickEvent extends BaseEvent
{
	private final ScheduledTick<Block> tileTickEntry;
	public ExecuteTileTickEvent(EventType eventType, ScheduledTick<Block> tileTickEntry)
	{
		super(eventType, "execute_tile_tick");
		this.tileTickEntry = tileTickEntry;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(MicroTickUtil.getTranslatedName(this.tileTickEntry.getObject()));
		list.add("q  Execute");
		list.add("c  TileTick");
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add("q  ended");
		}
		list.add(String.format("^w Priority: %d (%s)", this.tileTickEntry.priority.getIndex(), this.tileTickEntry.priority));
		return Messenger.c(list.toArray(new Object[0]));
	}


	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ExecuteTileTickEvent)) return false;
		if (!super.equals(o)) return false;
		ExecuteTileTickEvent that = (ExecuteTileTickEvent) o;
		return Objects.equals(tileTickEntry, that.tileTickEntry);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), tileTickEntry);
	}
}
