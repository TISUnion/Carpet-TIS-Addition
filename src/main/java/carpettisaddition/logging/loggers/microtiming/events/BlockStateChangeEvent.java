package carpettisaddition.logging.loggers.microtiming.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

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

	private BaseText getChangesText(char header, boolean justShowMeDetail)
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
			BaseText simpleText = Messenger.c(
					String.format("w %s", change.name),
					"g =",
					MicroTimingUtil.getColoredValue(change.newValue)
			);
			BaseText detailText = Messenger.c(
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
				changes.add(Util.getFancyText(null, simpleText, detailText, null));
			}
		}
		return Messenger.c(changes.toArray(new Object[0]));
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(this.getEnclosedTranslatedBlockNameHeaderText(this.block));
		BaseText titleText = Messenger.c(COLOR_ACTION + this.tr("State Change"));
		if (this.getEventType() != EventType.ACTION_END)
		{
			list.add(titleText);
			list.add("g : ");
			list.add(this.getChangesText(' ', false));
		}
		else
		{
			list.add(Util.getFancyText(
					"w",
					Messenger.c(
							titleText,
							Util.getSpaceText(),
							COLOR_RESULT + this.tr("finished")
					),
					Messenger.c(
							String.format("w %s", this.tr("Changed BlockStates")),
							"w :\n",
							this.getChangesText('\n', true),
							"w  "
					),
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
		super.mergeQuitEvent(quitEvent);
		if (quitEvent instanceof BlockStateChangeEvent)
		{
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
