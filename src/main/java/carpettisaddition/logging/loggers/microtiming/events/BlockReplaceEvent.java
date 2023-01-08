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
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.text.BaseText;

import java.util.List;

public class BlockReplaceEvent extends AbstractSetblockStateEvent
{
	public BlockReplaceEvent(EventType eventType, BlockState oldBlockState, BlockState newBlockState, Boolean returnValue,  int flags)
	{
		super(eventType, "block_replace", oldBlockState, newBlockState, returnValue, flags);
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseText titleText = Messenger.fancy(
				Messenger.formatting(tr("block_replace"), COLOR_ACTION),
				this.getFlagsText(),
				null
		);
		BaseText infoText = Messenger.c(
				Messenger.block(this.oldBlockState),
				"g ->",
				Messenger.block(this.newBlockState)
		);
		if (this.getEventType() != EventType.ACTION_END)
		{
			list.add(Messenger.c(titleText, "g : ", infoText));
		}
		else
		{
			list.add(Messenger.fancy(
					"w",
					Messenger.c(titleText, Messenger.getSpaceText(), Messenger.formatting(tr("finished"), COLOR_RESULT)),
					infoText,
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
}
