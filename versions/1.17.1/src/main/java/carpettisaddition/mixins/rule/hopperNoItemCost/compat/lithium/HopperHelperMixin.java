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

package carpettisaddition.mixins.rule.hopperNoItemCost.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.scounter.SupplierCounterCommand;
import carpettisaddition.helpers.rule.hopperNoItemCost.HopperNoItemCostHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.lithium.common.hopper.HopperHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DyeColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"),
		@Condition(ModIds.lithium)
})
@Mixin(HopperHelper.class)
public abstract class HopperHelperMixin
{
	@Unique
	private static ItemStack originalTransferStack$TISCM = null;

	@ModifyVariable(
			//#if MC >= 12101
			//$$ method = "tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/WorldlyContainer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Z",
			//#else
			method = "tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/WorldlyContainer;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Z",
			//#endif
			at = @At(value = "HEAD"),
			argsOnly = true,
			index = 2
	)
	private static ItemStack hopperNoItemCost(ItemStack transferStack)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost && HopperNoItemCostHelper.woolColor.get() != null)
		{
			originalTransferStack$TISCM = transferStack;
			transferStack = transferStack.copy();
		}
		return transferStack;
	}

	@ModifyVariable(
			//#if MC >= 12101
			//$$ method = "tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/WorldlyContainer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Z",
			//#else
			method = "tryMoveSingleItem(Lnet/minecraft/world/Container;Lnet/minecraft/world/WorldlyContainer;Lnet/minecraft/world/item/ItemStack;ILnet/minecraft/core/Direction;)Z",
			//#endif
			at = @At(value = "RETURN"),
			argsOnly = true,
			index = 2
	)
	private static ItemStack hopperNoItemCost_supplierCounter(ItemStack transferStack)
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			DyeColor woolColor = HopperNoItemCostHelper.woolColor.get();
			if (woolColor != null && originalTransferStack$TISCM != null)
			{
				if (SupplierCounterCommand.getInstance().isActivated())
				{
					SupplierCounterCommand.getInstance().record(woolColor, originalTransferStack$TISCM, transferStack);
				}
				originalTransferStack$TISCM = null;
			}
		}
		return transferStack;
	}
}