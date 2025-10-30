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
import com.google.common.collect.Maps;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.ChatFormatting;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class BlockStateChangeEvent extends AbstractSetblockStateEvent
{
	private final Map<Property<?>, PropertyChange> changes = Maps.newLinkedHashMap();

	public BlockStateChangeEvent(EventType eventType, BlockState oldBlockState, BlockState newBlockState, Boolean returnValue, int flags)
	{
		super(eventType, "block_state_change", oldBlockState, newBlockState, returnValue, flags);
	}

	public void setChanges(Collection<PropertyChange> changes)
	{
		this.changes.clear();
		changes.forEach(change -> this.changes.put(change.property, change));
	}

	private BaseComponent getChangesText(boolean isHover)
	{
		Function<@Nullable Property<?>, BaseComponent> hoverMaker = currentProperty -> {
			List<BaseComponent> lines = Lists.newArrayList();
			lines.add(Messenger.formatting(tr("state_change_details"), ChatFormatting.BOLD));
			this.oldBlockState.getProperties().stream().
					map(property -> {
						BaseComponent text = Optional.ofNullable(this.changes.get(property)).
								map(PropertyTexts::change).
								orElseGet(() -> {
									Object value = this.oldBlockState.getValue(property);
									return PropertyTexts.value(": ", property, value);
								});
						if (property.equals(currentProperty))
						{
							text.append(Messenger.s("    <---", ChatFormatting.GRAY));
						}
						return text;
					}).
					forEach(lines::add);
			return Messenger.join(Messenger.s("\n"), lines.toArray(new BaseComponent[0]));
		};
		if (isHover)
		{
			return hoverMaker.apply(null);
		}
		else
		{
			return Messenger.join(
					Messenger.s(" "),
					this.changes.values().stream().
							map(change -> Messenger.hover(
									PropertyTexts.value("=", change.property, change.newValue),
									hoverMaker.apply(change.property)
							)).
							toArray(BaseComponent[]::new)
			);
		}
	}

	@Override
	public BaseComponent toText()
	{
		List<Object> list = Lists.newArrayList();
		BaseComponent titleText = Messenger.fancy(
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
					this.getChangesText(false)
			));
		}
		else
		{
			list.add(Messenger.fancy(
					Messenger.c(titleText, Messenger.getSpaceText(), Messenger.formatting(tr("finished"), COLOR_RESULT)),
					this.getChangesText(true),
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

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		BlockStateChangeEvent that = (BlockStateChangeEvent) o;
		return Objects.equals(changes, that.changes);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), changes);
	}

	public static class PropertyTexts
	{
		// xxx${divider}aaa
		public static BaseComponent value(String divider, Property<?> property, Object value)
		{
			return Messenger.c(
					Messenger.s(property.getName()),
					"g " + divider,
					Messenger.property(property, value)
			);
		}

		// xxx: aaa->bbb
		public static BaseComponent change(Property<?> property, Object oldValue, Object newValue)
		{
			return Messenger.c(
					Messenger.s(property.getName()),
					"g : ",
					Messenger.property(property, oldValue),
					"g ->",
					Messenger.property(property, newValue)
			);
		}

		public static BaseComponent change(PropertyChange propertyChange)
		{
			return change(propertyChange.property, propertyChange.oldValue, propertyChange.newValue);
		}
	}

	public static class PropertyChange
	{
		public final Property<?> property;
		public final Object oldValue;
		public final Object newValue;

		public PropertyChange(Property<?> property, Object oldValue, Object newValue)
		{
			this.property = property;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (!(o instanceof PropertyChange)) return false;
			PropertyChange changes = (PropertyChange) o;
			return Objects.equals(property, changes.property) &&
					Objects.equals(oldValue, changes.oldValue) &&
					Objects.equals(newValue, changes.newValue);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(property, oldValue, newValue);
		}
	}
}
