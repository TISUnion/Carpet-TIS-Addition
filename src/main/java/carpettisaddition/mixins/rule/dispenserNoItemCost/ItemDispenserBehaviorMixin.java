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
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemDispenserBehavior.class)
public abstract class ItemDispenserBehaviorMixin
{
	@Shadow
	protected abstract ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack);

	@Redirect(
			method = "dispense",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/dispenser/ItemDispenserBehavior;dispenseSilently(Lnet/minecraft/util/math/BlockPointer;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"
			)
	)
	private ItemStack injectNoCostBehavior(ItemDispenserBehavior itemDispenserBehavior, BlockPointer pointer, ItemStack stack)
	{
		if (CarpetTISAdditionSettings.dispenserNoItemCost)
		{
			this.dispenseSilently(pointer, stack.copy());
			return stack;
		}
		else
		{
			return this.dispenseSilently(pointer, stack);
		}
	}
}
