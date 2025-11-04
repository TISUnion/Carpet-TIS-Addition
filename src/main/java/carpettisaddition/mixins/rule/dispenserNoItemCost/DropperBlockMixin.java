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

package carpettisaddition.mixins.rule.dispenserNoItemCost;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DropperBlock.class)
public abstract class DropperBlockMixin
{
	@ModifyArg(
			method = "dispenseFrom",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/core/dispenser/DispenseItemBehavior;dispense(Lnet/minecraft/core/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
			)
	)
	private ItemStack dispenserNoItemCost_useACopySoItCostNothing(ItemStack stack)
	{
		if (CarpetTISAdditionSettings.dispenserNoItemCost)
		{
			stack = stack.copy();
		}
		return stack;
	}

	@ModifyExpressionValue(
			method = "dispenseFrom",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/entity/HopperBlockEntity;getContainerAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/Container;"
			)
	)
	private Container dispenserNoItemCost_checkIfInventoryIsNullToDetermineLogic(Container inventory, @Share("currentDispenseIsItemDispense") LocalBooleanRef currentDispenseIsItemDispense)
	{
		currentDispenseIsItemDispense.set(inventory == null);
		return inventory;
	}

	@WrapOperation(
			method = "dispenseFrom",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/entity/DispenserBlockEntity;setItem(ILnet/minecraft/world/item/ItemStack;)V"
			)
	)
	private void dispenserNoItemCost_dontSetBackTheConsumedStack(
			DispenserBlockEntity blockEntity, int slot, ItemStack stack, Operation<Void> original,
			@Share("currentDispenseIsItemDispense") LocalBooleanRef currentDispenseIsItemDispense)
	{
		if (CarpetTISAdditionSettings.dispenserNoItemCost && currentDispenseIsItemDispense.get())  // not transfer mode
		{
			return;
		}

		// vanilla call
		original.call(blockEntity, slot, stack);
	}
}
