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

import carpettisaddition.helpers.rule.largeBarrel.compat.malilib.LargeBarrelMasaModUtils;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(ModIds.malilib))
@Pseudo
@Mixin(targets = "fi.dy.masa.malilib.util.InventoryUtils")
public abstract class InventoryUtilsMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@ModifyReturnValue(
			method = "getInventory",
			at = @At(value = "RETURN", ordinal = 1),
			remap = false
	)
	private static Container letMalilibRecognizeLargeBarrel(Container inventory, Level world, BlockPos pos)
	{
		return LargeBarrelMasaModUtils.modifyGetBlockInventoryReturnValue(inventory, world, pos);
	}
}
