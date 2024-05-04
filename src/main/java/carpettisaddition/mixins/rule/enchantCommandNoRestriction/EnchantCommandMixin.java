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

package carpettisaddition.mixins.rule.enchantCommandNoRestriction;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.server.command.EnchantCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collection;
import java.util.Collections;

@Mixin(value = EnchantCommand.class, priority = 900)
public abstract class EnchantCommandMixin
{
	@ModifyExpressionValue(
			method = "execute",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/enchantment/Enchantment;getMaximumLevel()I"
			)
	)
	private static int enchantCommandNoRestriction_removeLevelRestriction(int maxLevel)
	{
		if (CarpetTISAdditionSettings.enchantCommandNoRestriction)
		{
			maxLevel = Integer.MAX_VALUE;
		}
		return maxLevel;
	}

	@ModifyExpressionValue(
			method = "execute",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/enchantment/Enchantment;isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z"
			)
	)
	private static boolean enchantCommandNoRestriction_removeAcceptableCheck(boolean isAcceptable)
	{
		if (CarpetTISAdditionSettings.enchantCommandNoRestriction)
		{
			isAcceptable = true;
		}
		return isAcceptable;
	}

	@ModifyArg(
			method = "execute",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12100
					//$$ target = "Lnet/minecraft/enchantment/EnchantmentHelper;isCompatible(Ljava/util/Collection;Lnet/minecraft/registry/entry/RegistryEntry;)Z"
					//#else
					target = "Lnet/minecraft/enchantment/EnchantmentHelper;contains(Ljava/util/Collection;Lnet/minecraft/enchantment/Enchantment;)Z"
					//#endif
			),
			index = 0
	)
	private static Collection<Enchantment> enchantCommandNoRestriction_removeAlreadyExistingCheck(Collection<Enchantment> enchantments)
	{
		if (CarpetTISAdditionSettings.enchantCommandNoRestriction)
		{
			return Collections.emptySet();
		}
		return enchantments;
	}
}
