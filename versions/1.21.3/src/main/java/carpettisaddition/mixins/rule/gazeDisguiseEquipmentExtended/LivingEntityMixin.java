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
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Used in mc1.21.2+
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	/**
	 * lambda method of static predicate {@link LivingEntity#PLAYER_NOT_WEARING_DISGUISE_ITEM}
	 */
	@ModifyExpressionValue(
			//#if MC >= 26.1
			//$$ method = "lambda$static$0",
			//#else
			method = "method_64620",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
			)
	)
	private static boolean gazeDisguiseEquipmentExtended_pretendThisIsACarvedPumpkin(boolean isCarvedPumpkin, @Local ItemStack playerWearingItem)
	{
		if (CarpetTISAdditionSettings.gazeDisguiseEquipmentExtended)
		{
			if (GazeDisguiseEquipmentExtendedHelper.isGazeDisguiseEquipmentExtendedItem(playerWearingItem))
			{
				isCarvedPumpkin = true;
			}
		}
		return isCarvedPumpkin;
	}
}
