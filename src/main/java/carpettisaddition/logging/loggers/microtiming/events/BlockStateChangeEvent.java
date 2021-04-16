package carpettisaddition.logging.loggers.microtiming.events;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BlockStateChangeEvent extends BaseEvent
{
	private final Block block;
	private Boolean returnValue;
	private final int flags;
	private final List<PropertyChanges> changes = Lists.newArrayList();

	private static final List<FlagData> SET_BLOCK_STATE_FLAGS = Lists.newArrayList();

	static
	{
		SET_BLOCK_STATE_FLAGS.add(new FlagData(1, "emits block updates", false));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(2, "updates listeners", false));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(4, "updates client listeners", true));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(8, null, false));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(16, "emits state updates", true));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(32, null, false));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(64, "caused by piston", false));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(128, null, false));
	}

	public BlockStateChangeEvent(EventType eventType, Boolean returnValue, Block block, int flags)
	{
		super(eventType, "block_state_change", block);
		this.returnValue = returnValue;
		this.flags = flags;
		this.block = block;
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
				changes.add(TextUtil.getFancyText(null, simpleText, detailText, null));
			}
		}
		return Messenger.c(changes.toArray(new Object[0]));
	}

	private BaseText getFlagsText()
	{
		String bits = Integer.toBinaryString(this.flags);
		bits = String.join("", Collections.nCopies(Math.max(SET_BLOCK_STATE_FLAGS.size() - bits.length(), 0), "0")) + bits;
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.s(String.format("setBlockState flags = %d (%s)", this.flags, bits)));
		for (FlagData flagData: SET_BLOCK_STATE_FLAGS)
		{
			if (flagData.isValid())
			{
				int currentBit = (this.flags & flagData.mask) > 0 ? 1 : 0;
				list.add(Messenger.c(
						String.format("w \nbit %d = %d: ", flagData.bitPos, currentBit),
						String.format("^w 2^%d = %d", flagData.bitPos, flagData.mask),
						MicroTimingUtil.getSuccessText((currentBit ^ flagData.revert) != 0, false),
						"w  ",
						Messenger.s(this.tr("flag_data." + flagData.bitPos, flagData.detail))
				));
			}
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseText titleText = TextUtil.getFancyText(
				null,
				Messenger.c(COLOR_ACTION + this.tr("State Change")),
				this.getFlagsText(),
				null
		);
		if (this.getEventType() != EventType.ACTION_END)
		{
			list.add(titleText);
			list.add("g : ");
			list.add(this.getChangesText(' ', false));
		}
		else
		{
			list.add(TextUtil.getFancyText(
					"w",
					Messenger.c(
							titleText,
							TextUtil.getSpaceText(),
							COLOR_RESULT + this.tr("finished")
					),
					Messenger.c(
							String.format("w %s", this.tr("Changed BlockStates")),
							"w :\n",
							this.getChangesText('\n', true)
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

	private static class FlagData
	{
		private final int mask;
		private final String detail;
		private final int revert;
		private final int bitPos;

		private FlagData(int mask, String detail, boolean revert)
		{
			if (mask <= 0)
			{
				throw new IllegalArgumentException(String.format("mask = %d < 0", mask));
			}
			this.mask = mask;
			this.detail = detail;
			this.revert = revert ? 1 : 0;
			int pos = 0;
			for (int n = this.mask; n > 0; n >>= 1)
			{
				pos++;
			}
			this.bitPos = pos - 1;
		}

		private boolean isValid()
		{
			return this.detail != null;
		}
	}
}
