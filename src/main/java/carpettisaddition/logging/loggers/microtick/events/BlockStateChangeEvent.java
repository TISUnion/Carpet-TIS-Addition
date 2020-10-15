package carpettisaddition.logging.loggers.microtick.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtick.types.EventType;
import carpettisaddition.logging.loggers.microtick.utils.MicroTickUtil;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Objects;

public class BlockStateChangeEvent extends BaseEvent
{
	private final Block block;
	private Boolean returnValue;
	private final List<PropertyChanges> changes = Lists.newArrayList();

	public BlockStateChangeEvent(EventType eventType, Boolean returnValue, Block block)
	{
		super(eventType, "block_state_change");
		this.block = block;
		this.returnValue = returnValue;
	}

	private BaseText getColoredValue(Object value)
	{
		BaseText text = Messenger.s(value.toString());
		if (Boolean.TRUE.equals(value))
		{
			text.getStyle().setColor(Formatting.GREEN);
		}
		else if (Boolean.FALSE.equals(value))
		{
			text.getStyle().setColor(Formatting.RED);
		}
		return text;
	}

	private BaseText getChangesText(char header)
	{
		List<Object> changes = Lists.newArrayList();
		for (PropertyChanges change : this.changes)
		{
			changes.add(String.format("w %c%s: ", header, change.name));
			changes.add(this.getColoredValue(change.oldValue));
			changes.add("g ->");
			changes.add(this.getColoredValue(change.newValue));
		}
		return Messenger.c(changes.toArray(new Object[0]));
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(MicroTickUtil.getTranslatedName(block));
		if (this.getEventType() != EventType.ACTION_END)
		{
			list.add(this.getChangesText(' '));
		}
		else
		{
			;
			list.add(Util.getFancyText(
					"w",
					Messenger.c("q  " + this.tr("BlockState Change")),
					(BaseText)Messenger.s(this.tr("Changed states")).append(this.getChangesText('\n')),
					null
			));
		}
		if (this.returnValue != null)
		{
			list.add(Messenger.s(" "));
			list.add(MicroTickUtil.getSuccessText(this.returnValue));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	public void addChanges(String name, Object oldValue, Object newValue)
	{
		if (!oldValue.equals(newValue))
		{
			this.changes.add(new PropertyChanges(name, oldValue, newValue));
		}
	}

	public boolean hasChanges()
	{
		return !this.changes.isEmpty();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof BlockStateChangeEvent)) return false;
		if (!super.equals(o)) return false;
		BlockStateChangeEvent that = (BlockStateChangeEvent) o;
		return Objects.equals(block, that.block) &&
				Objects.equals(returnValue, that.returnValue) &&
				Objects.equals(changes, that.changes);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), block, returnValue, changes);
	}

	@Override
	public void mergeQuitEvent(BaseEvent quitEvent)
	{
		if (quitEvent instanceof BlockStateChangeEvent)
		{
			super.mergeQuitEvent(quitEvent);
			this.returnValue = ((BlockStateChangeEvent)quitEvent).returnValue;
		}
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
