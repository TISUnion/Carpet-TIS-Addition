/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.dropperNoItemCost;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DropItemDispenseBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc >= 26.4: subproject 26.4                   <--------
 * mc <  26.4: subproject 1.15.2 (main project)
 *
 * Container transfers use a separate behavior. Only the world drop splits the stack here.
 */
@Mixin(DropItemDispenseBehavior.class)
public abstract class DropperBlockMixin
{
	@ModifyReceiver(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;split(I)Lnet/minecraft/world/item/ItemStack;"))
	private ItemStack dropperNoItemCost_dropFromCopy(ItemStack stack, int amount, @Local(argsOnly = true) BlockSource source)
	{
		if (CarpetTISAdditionSettings.dropperNoItemCost && source.blockEntity() instanceof DropperBlockEntity)
		{
			return stack.copy();
		}
		return stack;
	}
}
