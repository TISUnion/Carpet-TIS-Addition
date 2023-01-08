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
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//#if MC >= 11700
//$$ import net.minecraft.block.BlockState;
//#else
import org.spongepowered.asm.mixin.Shadow;
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

	//#if MC < 11700
	@Shadow public abstract double getHopperX();
	@Shadow public abstract double getHopperY();
	@Shadow public abstract double getHopperZ();
	//#endif

	@Inject(
			method = "insert",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/inventory/Inventory;markDirty()V",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILSOFT
	)
	//#if MC >= 11700
	//$$ private static void hopperNoItemCost(World world, BlockPos pos, BlockState state, Inventory inventory, CallbackInfoReturnable<Boolean> cir, Inventory inventory2, Direction direction, int i, ItemStack itemStack, ItemStack itemStack2)
	//#else
	private void hopperNoItemCost(CallbackInfoReturnable<Boolean> cir, Inventory inventory, Direction direction, int i, ItemStack itemStack, ItemStack itemStack2)
	//#endif
	{
		if (CarpetTISAdditionSettings.hopperNoItemCost)
		{
			//#if MC < 11700
			World world = this.getWorld();
			//#endif

			if (world != null)
			{
				BlockPos hopperPos =
						//#if MC >= 11700
						//$$ pos;
						//#else
						new BlockPos(this.getHopperX(), this.getHopperY(), this.getHopperZ());
						//#endif

				DyeColor wool_color = WoolTool.getWoolColorAtPosition(world, hopperPos.offset(Direction.UP));
				if (wool_color != null)
				{
					Inventory hopperInventory =
							//#if MC >= 11700
							//$$ inventory;
							//#else
							this;
							//#endif

					if (SupplierCounterCommand.isActivated())
					{
						SupplierCounterCommand.getInstance().record(wool_color, itemStack, hopperInventory.getInvStack(i));
					}

					// restore the hopper inventory slot
					hopperInventory.setInvStack(i, itemStack);
				}
			}
		}
	}
}
