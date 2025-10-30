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

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.entity.vehicle.MinecartHopper;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;

import java.util.Optional;

public class MinecartFullDropBackportHelper
{
	public static Optional<ItemLike> getFullDropItem(AbstractMinecart cart)
	{
		ItemLike item = null;
		if (cart instanceof MinecartChest)
		{
			item = Items.CHEST_MINECART;
		}
		else if (cart instanceof MinecartFurnace)
		{
			item = Items.FURNACE_MINECART;
		}
		else if (cart instanceof MinecartHopper)
		{
			item = Items.HOPPER_MINECART;
		}
		else if (cart instanceof MinecartTNT)
		{
			item = Items.TNT_MINECART;
		}
		return Optional.ofNullable(item);
	}
}
