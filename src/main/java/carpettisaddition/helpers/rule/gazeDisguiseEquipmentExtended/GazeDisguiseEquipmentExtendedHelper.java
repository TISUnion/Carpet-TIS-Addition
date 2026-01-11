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

package carpettisaddition.helpers.rule.gazeDisguiseEquipmentExtended;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

//#if MC >= 1.20.5
//$$ import net.minecraft.tags.ItemTags;
//#endif

public class GazeDisguiseEquipmentExtendedHelper
{
	@SuppressWarnings("RedundantIfStatement")
	public static boolean isGazeDisguiseEquipmentExtendedItem(ItemStack itemStack)
	{
		Item item = itemStack.getItem();

		// vanilla
		//#if MC >= 1.21.2
		//$$ if (itemStack.is(ItemTags.GAZE_DISGUISE_EQUIPMENT))
		//$$ {
		//$$ 	return true;
		//$$ }
		//#else
		if (item == Items.CARVED_PUMPKIN)
		{
			return true;
		}
		//#endif

		// extended (skull)
		//#if MC >= 1.20.5
		//$$ if (itemStack.is(ItemTags.SKULLS))
		//$$ {
		//$$ 	return true;
		//$$ }
		//#else
		if (
				item == Items.PLAYER_HEAD || item == Items.CREEPER_HEAD || item == Items.ZOMBIE_HEAD || item == Items.SKELETON_SKULL || item == Items.WITHER_SKELETON_SKULL || item == Items.DRAGON_HEAD
				//#if MC >= 1.19.3
				//$$ || item == Items.PIGLIN_HEAD
				//#endif
		)
		{
			return true;
		}
		//#endif

		// not matched
		return false;
	}
}
