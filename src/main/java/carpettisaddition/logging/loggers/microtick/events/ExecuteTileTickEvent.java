package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.world.ScheduledTick;
import net.minecraft.world.World;

import java.util.List;

public class ExecuteTileTickEvent extends BaseEvent
{
	private final ScheduledTick<Block> tileTickEntry;
	public ExecuteTileTickEvent(World world, EventType eventType, ScheduledTick<Block> tileTickEntry)
	{
		super(world, eventType, "execute_tile_tick");
		this.tileTickEntry = tileTickEntry;
	}

	@Override
	public Text toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(MicroTickUtil.getTranslatedName(this.tileTickEntry.getObject()));
		list.add("q  Execute");
		list.add("c  TileTick");
		if (eventType == EventType.ACTION_END)
		{
			list.add(String.format("q  %s", eventType));
		}
		list.add(String.format("^w Priority: %d (%s)", this.tileTickEntry.priority.getIndex(), this.tileTickEntry.priority));
		return Messenger.c(list.toArray(new Object[0]));
	}
}
