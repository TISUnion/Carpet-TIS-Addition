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

package carpettisaddition.mixins.rule.hopperNoItemCost;

import carpet.utils.WoolTool;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.scounter.SupplierCounterCommand;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11700
//$$ import net.minecraft.block.BlockState;
//#endif

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity
{
	//#if MC >= 11700
	//$$ protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState)
	//$$ {
	//$$ 	super(blockEntityType, blockPos, blockState);
	//$$ }
	//#else
	protected HopperBlockEntityMixin(BlockEntityType<?> blockEntityType)
	{
		super(blockEntityType);
	}
	//#endif

	@Inject(
			method = "insert",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/inventory/Inventory;markDirty()V",
					shift = At.Shift.AFTER
			)
	)
	//#if MC >= 11700
	//$$ private static
	//#else
	private
	//#endif
	void hopperNoItemCost_hopperOutputHook(
			CallbackInfoReturnable<Boolean> cir,
			//#if MC >= 11700
			//$$ @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos,
			//$$ //#if MC >= 12005
			//$$ //$$ @Local(argsOnly = true) HopperBlockEntity hopperInventory,
			//$$ //#else
			//$$ @Local(argsOnly = true) Inventory hopperInventory,
			//$$ //#endif
			//#endif
			@Local(ordinal = 0) int index, @Local(ordinal = 0) ItemStack itemStack
			//#if MC >= 12005
			//$$ , @Local(ordinal = 1) int prevCount
			//#endif
	)
	{
		if (!CarpetTISAdditionSettings.hopperNoItemCost)
		{
			return;
		}

		//#if MC < 11700
		World world = this.getWorld();
		Inventory hopperInventory = this;
		//#endif

		if (world == null)
		{
			return;
		}
		BlockPos hopperPos =
				//#if MC >= 11700
				//$$ pos;
				//#else
				this.getPos();
				//#endif
		DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, hopperPos.offset(Direction.UP));
		if (wool_color == null)
		{
			return;
		}

		//#if MC >= 12005
		//$$ int currentCount = itemStack.getCount();
		//$$ itemStack.setCount(prevCount);  // NOTES: empty_stack.copy() == EMPTY, so we need to setCount() before copy()
		//$$ ItemStack prevStack = itemStack.copy();
		//$$ itemStack.setCount(currentCount);
		//#else
		ItemStack prevStack = itemStack;  // itemStack is already a copy, see vanilla insert() method
		//#endif
		ItemStack currentStack = hopperInventory.getInvStack(index);

		if (SupplierCounterCommand.getInstance().isActivated())
		{
			SupplierCounterCommand.getInstance().record(wool_color, prevStack, currentStack);
		}

		// restore the hopper inventory slot to the previous stack
		hopperInventory.setInvStack(index, prevStack);
	}
}
