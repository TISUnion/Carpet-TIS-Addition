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

package carpettisaddition.mixins.rule.gazeDisguiseEquipmentExtended;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.gazeDisguiseEquipmentExtended.GazeDisguiseEquipmentExtendedHelper;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 1.17
//$$ import net.minecraft.world.item.Item;
//#endif

/**
 * Used in mc1.21.2-
 */
@Mixin(EnderMan.class)
public abstract class EnderManMixin
{
	@ModifyReceiver(
			method = "isLookingAtMe",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.17
					//$$ target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
					//#else
					target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
					//#endif
			)
	)
	private ItemStack gazeDisguiseEquipmentExtended_pretendThisIsACarvedPumpkin(
			ItemStack playerWearingItemStack
			//#if MC >= 1.17
			//$$ , Item item
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.gazeDisguiseEquipmentExtended)
		{
			if (GazeDisguiseEquipmentExtendedHelper.isGazeDisguiseEquipmentExtendedItem(playerWearingItemStack))
			{
				playerWearingItemStack = new ItemStack(Items.CARVED_PUMPKIN);
			}
		}
		return playerWearingItemStack;
	}
}
