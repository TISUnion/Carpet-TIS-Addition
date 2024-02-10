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

package carpettisaddition.mixins.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin
{
	@Inject(
			//#if MC >= 12005
			//$$ method = "getBlockInventoryAt",
			//#else
			method = "getInventoryAt(Lnet/minecraft/world/World;DDD)Lnet/minecraft/inventory/Inventory;",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;getBlockEntity(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/entity/BlockEntity;"
			),
			cancellable = true
	)
	private static void largeBarrel_useLargeBarrelInventoryIfPossible(
			CallbackInfoReturnable<Inventory> cir,
			@Local(argsOnly = true) World world,
			@Local BlockPos pos, @Local BlockState state,  // no argsOnly for mc < 1.20.5
			@Local Block block
	){
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			// inventory var not captured, so no inventory instanceof BarrelBlockEntity check, should be fine
			if (block instanceof BarrelBlock)
			{
				Inventory largeBarrel = LargeBarrelHelper.getInventory(state, world, pos);
				if (largeBarrel != null)
				{
					cir.setReturnValue(largeBarrel);
				}
			}
		}
	}
}
