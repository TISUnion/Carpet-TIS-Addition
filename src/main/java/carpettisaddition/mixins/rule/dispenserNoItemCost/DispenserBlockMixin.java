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
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

//#if MC >= 26.4
//$$ import net.minecraft.world.level.block.DropperBlock;
//#endif

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin
{
	@Unique
	private boolean shouldDispenseWithoutCost$TISCM()
	{
		//#if MC >= 26.4
		//$$ // Droppers now inherit dispenseFrom and have their own rule.
		//$$ return CarpetTISAdditionSettings.dispenserNoItemCost && !((Object)this instanceof DropperBlock);
		//#else
		return CarpetTISAdditionSettings.dispenserNoItemCost;
		//#endif
	}

	@ModifyArg(
			method = "dispenseFrom",
			at = @At(
					value = "INVOKE",
					//#if MC >= 26.4
					//$$ target = "Lnet/minecraft/core/dispenser/DispenseBehavior;dispense(Ljava/lang/Iterable;Lnet/minecraft/core/dispenser/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
					//#else
					target = "Lnet/minecraft/core/dispenser/DispenseItemBehavior;dispense(Lnet/minecraft/core/BlockSource;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/item/ItemStack;"
					//#endif
			)
	)
	private ItemStack dispenserNoItemCost_useACopySoItCostNothing(ItemStack stack)
	{
		if (this.shouldDispenseWithoutCost$TISCM())
		{
			stack = stack.copy();
		}
		return stack;
	}

	@WrapOperation(
			method = "dispenseFrom",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/entity/DispenserBlockEntity;setItem(ILnet/minecraft/world/item/ItemStack;)V"
			)
	)
	private void dispenserNoItemCost_dontSetBackTheConsumedStack(DispenserBlockEntity blockEntity, int slot, ItemStack stack, Operation<Void> original)
	{
		if (this.shouldDispenseWithoutCost$TISCM())
		{
			return;
		}

		// vanilla call
		original.call(blockEntity, slot, stack);
	}
}
