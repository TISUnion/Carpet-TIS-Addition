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

import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ScheduleBlockUpdateEvent extends AbstractBlockUpdateEvent
{
	public ScheduleBlockUpdateEvent(Block sourceBlock, BlockUpdateType blockUpdateType, Direction exceptSide)
	{
		super(EventType.EVENT, "schedule_block_update", sourceBlock, blockUpdateType, exceptSide);
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("scheduled"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		list.add(Messenger.fancy(
				Messenger.formatting(this.blockUpdateType.toText(), COLOR_TARGET),
				this.getUpdateTypeExtraMessage(),
				null
		));
		return Messenger.c(list.toArray(new Object[0]));
	}
}
