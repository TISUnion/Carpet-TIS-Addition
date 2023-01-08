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
import net.minecraft.block.Block;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Objects;

public class EmitBlockUpdateEvent extends BaseEvent
{
	private final Block block;
	private final String methodName;

	public EmitBlockUpdateEvent(EventType eventType, Block block, String methodName)
	{
		super(eventType, "emit_block_update", block);
		this.block = block;
		this.methodName = methodName;
	}

	@Override
	public boolean isImportant()
	{
		return false;
	}

	protected BaseText getUpdatesTextHoverText()
	{
		return Messenger.c(tr("method_name"), String.format("w : %s", this.methodName));
	}

	@Override
	public BaseText toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseText updatesText = Messenger.c(
				Messenger.formatting(tr("emit"), COLOR_ACTION),
				Messenger.getSpaceText(),
				Messenger.formatting(tr("updates"), COLOR_TARGET)
		);
		if (this.methodName != null)
		{
			Messenger.hover(updatesText, this.getUpdatesTextHoverText());
		}
		list.add(updatesText);
		switch (this.getEventType())
		{
			case ACTION_START:
				list.add(Messenger.getSpaceText());
				list.add(Messenger.formatting(tr("started"), COLOR_RESULT));
				break;
			case ACTION_END:
				list.add(Messenger.getSpaceText());
				list.add(Messenger.formatting(tr("ended"), COLOR_RESULT));
				break;
			default:
				break;
		}
		return Messenger.c(list.toArray(new Object[0]));
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (!(o instanceof EmitBlockUpdateEvent)) return false;
		if (!super.equals(o)) return false;
		EmitBlockUpdateEvent that = (EmitBlockUpdateEvent) o;
		return Objects.equals(block, that.block) &&
				Objects.equals(methodName, that.methodName);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), block, methodName);
	}
}
