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

package carpettisaddition.mixins.rule.largeBarrel.compat.malilib;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Restriction(require = @Condition(ModIds.malilib))
@Pseudo
@Mixin(targets = "fi.dy.masa.malilib.util.InventoryUtils")
public abstract class InventoryUtilsMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(
			method = "getInventory",
			at = @At(value = "RETURN", ordinal = 1),
			remap = false,
			cancellable = true
	)
	private static void letMalilibRecognizeLargBarrel(World world, BlockPos pos, CallbackInfoReturnable<Inventory> cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			Inventory inv = cir.getReturnValue();
			if (inv instanceof BarrelBlockEntity)
			{
				LargeBarrelHelper.enabledOffThreadBlockEntityAccess.set(true);
				try
				{
					Inventory largeBarrelInventory = LargeBarrelHelper.getInventory(world.getBlockState(pos), world, pos);
					if (largeBarrelInventory != null)
					{
						cir.setReturnValue(largeBarrelInventory);
					}
				}
				finally
				{
					LargeBarrelHelper.enabledOffThreadBlockEntityAccess.set(false);
				}
			}
		}
	}
}
