/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractSetblockStateEvent extends BaseEvent
{
	protected final BlockState oldBlockState;
	protected final BlockState newBlockState;
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
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(0, false));  // NOTIFY_NEIGHBORS = 1
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(1, false));  // NOTIFY_LISTENERS = 2
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(2, true));   // NO_REDRAW = 4
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(3, false));  // REDRAW_ON_MAIN_THREAD = 8
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(4, true));   // FORCE_STATE = 16
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(5, true));   // SKIP_DROPS = 32
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(6, false));  // MOVED = 64
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(7, true));   // SKIP_LIGHTING_UPDATES = 128
		SET_BLOCK_STATE_FLAGS.add(FlagData.of(8, true));   // SKIP_BLOCK_ENTITY_SIDEEFFECTS = 256
	}

	protected AbstractSetblockStateEvent(EventType eventType, String translateKey, BlockState oldBlockState, BlockState newBlockState, @Nullable Boolean returnValue, int flags)
	{
		super(eventType, translateKey, oldBlockState.getBlock());
		this.oldBlockState = oldBlockState;
		this.newBlockState = newBlockState;
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
		if (quitEvent instanceof AbstractSetblockStateEvent)
		{
			this.returnValue = ((AbstractSetblockStateEvent)quitEvent).returnValue;
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		AbstractSetblockStateEvent that = (AbstractSetblockStateEvent) o;
		return flags == that.flags && Objects.equals(oldBlockState, that.oldBlockState) && Objects.equals(newBlockState, that.newBlockState) && Objects.equals(returnValue, that.returnValue);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), oldBlockState, newBlockState, flags, returnValue);
	}

	private static class FlagData
	{
		private final int mask;
		private final int revert;
		private final int bitPos;
		private final boolean valid;

		private FlagData(int bitPos, boolean revert, boolean valid)
		{
			this.valid = valid;
			this.revert = revert ? 1 : 0;
			this.mask = 1 << bitPos;
			this.bitPos = bitPos;
		}

		private static FlagData of(int bitPos, boolean revert)
		{
			return new FlagData(bitPos, revert, true);
		}

		private static FlagData dummy()
		{
			return new FlagData(0, false, false);
		}

		private boolean isValid()
		{
			return this.valid;
		}
	}
}
