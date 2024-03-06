/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.shulkerBoxCCEReintroduced;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxBlock.class)
public abstract class ShulkerBoxBlockMixin
{
	@WrapOperation(
			method = "getComparatorOutput",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/screen/ScreenHandler;calculateComparatorOutput(Lnet/minecraft/block/entity/BlockEntity;)I"
			)
	)
	private int shulkerBoxCCEReintroduced_castIt(BlockEntity blockEntity, Operation<Integer> original)
	{
		if (CarpetTISAdditionSettings.shulkerBoxCCEReintroduced)
		{
			return ScreenHandler.calculateComparatorOutput((Inventory)blockEntity);
		}
		return original.call(blockEntity);
	}
}
