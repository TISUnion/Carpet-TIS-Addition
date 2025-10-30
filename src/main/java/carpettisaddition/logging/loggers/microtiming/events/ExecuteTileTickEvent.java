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
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.level.TickPriority;
import net.minecraft.world.level.TickNextTickData;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ExecuteTileTickEvent<T> extends BaseEvent
{
	private final TickNextTickData<T> tileTickEntry;

	private ExecuteTileTickEvent(EventType eventType, TickNextTickData<T> tileTickEntry, EventSource source)
	{
		super(eventType, "execute_tile_tick", source);
		this.tileTickEntry = tileTickEntry;
	}

	public static Optional<ExecuteTileTickEvent<?>> createFrom(EventType eventType, TickNextTickData<?> tileTickEntry)
	{
		return EventSource.fromObject(tileTickEntry.getType()).map(eventSource -> new ExecuteTileTickEvent<>(eventType, tileTickEntry, eventSource));
	}

	@Override
	public BaseComponent toText()
	{
		TickPriority priority =
				//#if MC >= 11800
				//$$ this.tileTickEntry.priority();
				//#else
				this.tileTickEntry.priority;
				//#endif

		List<Object> list = Lists.newArrayList();
		list.add(Messenger.formatting(tr("execute"), COLOR_ACTION));
		list.add(Messenger.getSpaceText());
		list.add(Messenger.fancy(
				Messenger.formatting(tr("tiletick_event"), COLOR_TARGET),
				Messenger.c(
						tr("priority"),
						String.format("w : %d (%s)", priority.getValue(), priority)
				),
				null
		));
		if (this.getEventType() == EventType.ACTION_END)
		{
			list.add(Messenger.getSpaceText());
			list.add(Messenger.formatting(tr("ended"), COLOR_RESULT));
		}
		return Messenger.c(list.toArray(new Object[0]));
	}


	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof ExecuteTileTickEvent)) return false;
		if (!super.equals(o)) return false;
		ExecuteTileTickEvent<?> that = (ExecuteTileTickEvent<?>) o;
		return Objects.equals(tileTickEntry, that.tileTickEntry);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), tileTickEntry);
	}
}
