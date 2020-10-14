package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.MicroTickUtil;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class BlockStateChangeEvent extends BaseEvent
{
	private final Block block;
	private final List<PropertyChanges> changes = Lists.newArrayList();

	public BlockStateChangeEvent(World world, EventType eventType, Block block)
	{
		super(world, eventType, "block_state_change");
		this.block = block;
	}

	@Override
	public Text toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(MicroTickUtil.getTranslatedName(block));
		if (this.eventType != EventType.ACTION_END)
		{
			for (PropertyChanges change : this.changes)
			{
				list.add(String.format("c  %s", change.name));
				list.add(String.format("w  %s", change.oldValue));
				list.add("g ->");
				list.add(String.format("w %s", change.newValue));
			}
		}
		else
		{
			list.add("w  BlockState Change " + this.eventType);
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	public void addChanges(String name, Object oldValue, Object newValue)
	{
		this.changes.add(new PropertyChanges(name, oldValue, newValue));
	}

	public boolean hasChanges()
	{
		return !this.changes.isEmpty();
	}

	public static class PropertyChanges
	{
		public final String name;
		public final Object oldValue;
		public final Object newValue;

		public PropertyChanges(String name, Object oldValue, Object newValue)
		{
			this.name = name;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
	}
}
