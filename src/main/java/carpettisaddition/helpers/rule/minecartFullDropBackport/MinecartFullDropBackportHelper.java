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

package carpettisaddition.helpers.rule.minecartFullDropBackport;

import net.minecraft.entity.vehicle.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;

import java.util.Optional;

public class MinecartFullDropBackportHelper
{
	public static Optional<ItemConvertible> getFullDropItem(AbstractMinecartEntity cart)
	{
		ItemConvertible item = null;
		if (cart instanceof ChestMinecartEntity)
		{
			item = Items.CHEST_MINECART;
		}
		else if (cart instanceof FurnaceMinecartEntity)
		{
			item = Items.FURNACE_MINECART;
		}
		else if (cart instanceof HopperMinecartEntity)
		{
			item = Items.HOPPER_MINECART;
		}
		else if (cart instanceof TntMinecartEntity)
		{
			item = Items.TNT_MINECART;
		}
		return Optional.ofNullable(item);
	}
}
