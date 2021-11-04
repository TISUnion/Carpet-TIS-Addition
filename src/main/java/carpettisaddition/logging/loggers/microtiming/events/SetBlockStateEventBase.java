package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class SetBlockStateEventBase extends BaseEvent
{
	protected final int flags;
	@Nullable
	protected Boolean returnValue;

	private static final Translator TRANSLATOR = (new BaseEvent(null, "set_block_state_event_base", (EventSource)null) {
		@Override
		public BaseText toText()
		{
			throw new UnsupportedOperationException();
		}
	}).getTranslator();
	private static final List<FlagData> SET_BLOCK_STATE_FLAGS = Lists.newArrayList();

	static
	{
		SET_BLOCK_STATE_FLAGS.add(new FlagData(1, false, true));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(2, false, true));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(4, true, true));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(8, false, false));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(16, true, true));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(32, false, false));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(64, false, true));
		SET_BLOCK_STATE_FLAGS.add(new FlagData(128, false, false));
	}

	protected SetBlockStateEventBase(EventType eventType, String translateKey, Block eventSourceBlock, @Nullable Boolean returnValue, int flags)
	{
		super(eventType, translateKey, eventSourceBlock);
		this.returnValue = returnValue;
		this.flags = flags;
	}

	protected BaseText getFlagsText()
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
						TRANSLATOR.tr("flag_data." + flagData.bitPos)
				));
			}
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public void mergeQuitEvent(BaseEvent quitEvent)
	{
		super.mergeQuitEvent(quitEvent);
		if (quitEvent instanceof SetBlockStateEventBase)
		{
			this.returnValue = ((SetBlockStateEventBase)quitEvent).returnValue;
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		SetBlockStateEventBase that = (SetBlockStateEventBase) o;
		return flags == that.flags && Objects.equals(returnValue, that.returnValue);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), flags, returnValue);
	}

	private static class FlagData
	{
		private final int mask;
		private final int revert;
		private final int bitPos;
		private final boolean valid;

		private FlagData(int mask, boolean revert, boolean valid)
		{
			this.valid = valid;
			if (mask <= 0)
			{
				throw new IllegalArgumentException(String.format("mask = %d < 0", mask));
			}
			this.mask = mask;
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
			return this.valid;
		}
	}
}
