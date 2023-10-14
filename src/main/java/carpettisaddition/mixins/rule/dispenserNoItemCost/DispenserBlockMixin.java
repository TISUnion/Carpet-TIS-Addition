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
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin
{
	@ModifyArg(
			method = "dispense",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/dispenser/DispenserBehavior;dispense(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
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

	@WrapOperation(
			method = "dispense",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/entity/DispenserBlockEntity;setInvStack(ILnet/minecraft/item/ItemStack;)V"
			)
	)
	private void dispenserNoItemCost_dontSetBackTheConsumedStack(DispenserBlockEntity blockEntity, int slot, ItemStack stack, Operation<Void> original)
	{
		if (CarpetTISAdditionSettings.dispenserNoItemCost)
		{
			return;
		}

		// vanilla call
		original.call(blockEntity, slot, stack);
	}
}
