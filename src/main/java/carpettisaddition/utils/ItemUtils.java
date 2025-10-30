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

package carpettisaddition.utils;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

//#if MC >= 12005
//$$ import com.google.common.collect.Maps;
//$$ import net.minecraft.component.DataComponentTypes;
//#endif

public class ItemUtils
{
	public static boolean hasCustomName(ItemStack itemStack)
	{
		//#if MC >= 12005
		//$$ return itemStack.get(DataComponentTypes.CUSTOM_NAME) != null;
		//#else
		return itemStack.hasCustomHoverName();
		//#endif
	}

	public static Map<Enchantment, Integer> getEnchantments(ItemStack itemStack)
	{
		//#if MC >= 12005
		//$$ Map<Enchantment, Integer> enchantments = Maps.newHashMap();
		//$$ var component = itemStack.get(DataComponentTypes.ENCHANTMENTS);
		//$$ if (component != null)
		//$$ {
		//$$ 	component.getEnchantmentsMap().forEach(entry -> {
		//$$ 		enchantments.put(entry.getKey().value(), entry.getIntValue());
		//$$ 	});
		//$$ }
		//$$ return enchantments;
		//#else
		return EnchantmentHelper.getEnchantments(itemStack);
		//#endif
	}
}
