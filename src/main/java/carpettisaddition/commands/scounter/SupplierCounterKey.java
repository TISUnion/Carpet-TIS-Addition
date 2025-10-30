/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.commands.scounter;

import carpettisaddition.commands.common.counter.DyeCounterKey;
import carpettisaddition.utils.Messenger;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.BaseComponent;

import java.util.Objects;

public class SupplierCounterKey implements DyeCounterKey
{
	private final Item item;

	public SupplierCounterKey(Item item)
	{
		this.item = item;
	}

	@Override
	public BaseComponent getText()
	{
		return Messenger.item(this.item);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SupplierCounterKey that = (SupplierCounterKey)o;
		return Objects.equals(item, that.item);
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(item);
	}
}
