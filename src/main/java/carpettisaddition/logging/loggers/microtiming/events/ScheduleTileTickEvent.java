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
import net.minecraft.text.BaseText;
import net.minecraft.world.TickPriority;

import java.util.List;
import java.util.Objects;

public class ScheduleTileTickEvent extends BaseEvent
{
	private final int delay;
	private final TickPriority priority;
	private final Boolean success;

	public ScheduleTileTickEvent(EventSource eventSource, int delay, TickPriority priority, Boolean success)
	{
		super(EventType.EVENT, "schedule_tile_tick", eventSource);
		this.delay = delay;
		this.priority = priority;
		this.success = success;
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("scheduled"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		list.add(Messenger.fancy(
				Messenger.formatting(tr("tiletick_event"), COLOR_TARGET),
				Messenger.c(
						tr("delay"), String.format("w : %dgt\n", this.delay),
						tr("priority"), String.format("w : %d (%s)", this.priority.getIndex(), this.priority)
				),
				null
		));
		if (this.success != null)
		{
			list.add("w  ");
			list.add(MicroTimingUtil.getSuccessText(this.success, false));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ScheduleTileTickEvent)) return false;
		if (!super.equals(o)) return false;
		ScheduleTileTickEvent that = (ScheduleTileTickEvent) o;
		return delay == that.delay &&
				priority == that.priority &&
				Objects.equals(success, that.success);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), delay, priority);
	}
}
