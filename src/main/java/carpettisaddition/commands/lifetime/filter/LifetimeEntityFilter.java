/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.commands.lifetime.filter;

import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.entityfilter.EntityFilter;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LifetimeEntityFilter implements Predicate<Entity>
{
	private final List<EntityFilter> filters;  // "or" relationship

	public LifetimeEntityFilter(List<EntityFilter> filters)
	{
		if (filters.isEmpty())
		{
			throw new IllegalArgumentException("Must have at least one selector");
		}
		this.filters = ImmutableList.copyOf(filters);
	}

	@Override
	public boolean test(Entity entity)
	{
		for (EntityFilter filter : this.filters)
		{
			if (filter.test(entity))
			{
				return true;
			}
		}
		return false;
	}

	public BaseComponent toText()
	{
		return Messenger.join(
				Messenger.hover(
						Messenger.s(" || ", ChatFormatting.DARK_GRAY),
						EntityFilterManager.getInstance().getTranslator().tr("or")
				),
				this.filters.stream().map(EntityFilter::toText).collect(Collectors.toList())
		);
	}
}
