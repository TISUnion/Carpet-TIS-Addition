package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.MutableText;

import java.util.List;
import java.util.Objects;

public class BlockStateChangeEvent extends SetBlockStateEventBase
{
	private final Block block;
	private final List<PropertyChanges> changes = Lists.newArrayList();

	public BlockStateChangeEvent(EventType eventType, Block block, Boolean returnValue, int flags)
	{
		super(eventType, "block_state_change", block, returnValue, flags);
		this.block = block;
	}

	private MutableText getChangesText(char header, boolean justShowMeDetail)
	{
		List<Object> changes = Lists.newArrayList();
		boolean isFirst = true;
		for (PropertyChanges change : this.changes)
		{
			if (!isFirst)
			{
				changes.add("w " + header);
			}
			isFirst = false;
			MutableText simpleText = Messenger.c(
					String.format("w %s", change.name),
					"g =",
					MicroTimingUtil.getColoredValue(change.newValue)
			);
			MutableText detailText = Messenger.c(
					String.format("w %s: ", change.name),
					MicroTimingUtil.getColoredValue(change.oldValue),
					"g ->",
					MicroTimingUtil.getColoredValue(change.newValue)
			);
			if (justShowMeDetail)
			{
				changes.add(detailText);
			}
			else
			{
				changes.add(Messenger.fancy(null, simpleText, detailText, null));
			}
		}
		return Messenger.c(changes.toArray(new Object[0]));
	}

	@Override
	public MutableText toText()
	{
		List<Object> list = Lists.newArrayList();
		MutableText titleText = Messenger.fancy(
				null,
				Messenger.formatting(tr("state_change"), COLOR_ACTION),
				this.getFlagsText(),
				null
		);
		if (this.getEventType() != EventType.ACTION_END)
		{
			list.add(Messenger.c(
					titleText,
					"g : ",
					this.getChangesText(' ', false)
			));
		}
		else
		{
			list.add(Messenger.fancy(
					Messenger.c(titleText, Messenger.getSpaceText(), Messenger.formatting(tr("finished"), COLOR_RESULT)),
					Messenger.c(tr("changed_states"), "w :\n", this.getChangesText('\n', true)),
					null
			));
		}
		if (this.returnValue != null)
		{
			list.add("w  ");
			list.add(MicroTimingUtil.getSuccessText(this.returnValue, true));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	public void addIfChanges(String name, Object oldValue, Object newValue)
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
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		BlockStateChangeEvent that = (BlockStateChangeEvent) o;
		return Objects.equals(block, that.block) && Objects.equals(changes, that.changes);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), block, changes);
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

		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (!(o instanceof PropertyChanges)) return false;
			PropertyChanges changes = (PropertyChanges) o;
			return Objects.equals(name, changes.name) &&
					Objects.equals(oldValue, changes.oldValue) &&
					Objects.equals(newValue, changes.newValue);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(name, oldValue, newValue);
		}
	}
}
