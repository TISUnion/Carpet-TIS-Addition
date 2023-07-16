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

package carpettisaddition.mixins.rule.minecartFullDropBackport;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.minecartFullDropBackport.MinecartFullDropBackportHelper;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.ItemConvertible;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Optional;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.19"))
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin
{
	@ModifyArg(
			method = "dropItems",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/item/ItemStack;<init>(Lnet/minecraft/item/ItemConvertible;)V"
			)
	)
	private ItemConvertible minecartFullDropBackport_modifyCartItem(ItemConvertible item)
	{
		if (CarpetTISAdditionSettings.minecartFullDropBackport)
		{
			Optional<ItemConvertible> fullDropItem = MinecartFullDropBackportHelper.getFullDropItem((AbstractMinecartEntity)(Object)this);
			if (fullDropItem.isPresent())
			{
				item = fullDropItem.get();
			}
		}

		return item;
	}
}
