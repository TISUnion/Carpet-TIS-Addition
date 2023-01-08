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

package carpettisaddition.mixins.rule.largeBarrel.compat.fabricapi;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.utils.ReflectionUtil;
import com.google.common.collect.Lists;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Restriction(require = @Condition("fabric-transfer-api-v1"))
@Pseudo
@Mixin(targets = "net.fabricmc.fabric.api.transfer.v1.item.ItemStorage")
public abstract class ItemStorageMixin
{
	@SuppressWarnings({"UnresolvedMixinReference", "rawtypes", "unchecked"})
	@Inject(
			method = "lambda$static$2",  // lambda method in clinit, Register Inventory fallback.
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	private static void TISCM_largeBarrelLookupLogic(World world, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction direction, CallbackInfoReturnable cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel && blockEntity instanceof BarrelBlockEntity)
		{
			Inventory largeBarrelInventory = LargeBarrelHelper.getInventory(state, world, pos);
			if (largeBarrelInventory instanceof DoubleInventory)
			{
				DoubleInventoryAccessor accessor = (DoubleInventoryAccessor)largeBarrelInventory;
				Object[] ret = new Object[]{null};

				try
				{
					ReflectionUtil.invoker("net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage", "of").
							ifPresent(invoker -> {
								Object first = invoker.apply(null, new Object[]{accessor.getFirst(), direction});
								Object second = invoker.apply(null, new Object[]{accessor.getSecond(), direction});


								ret[0] = ReflectionUtil.constructor("net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage", List.class).
										map(ctr -> ctr.apply(new Object[]{Lists.newArrayList(first, second)})).
										orElse(null);
							});
				}
				catch (ReflectionUtil.InvocationException ignored)
				{
				}

				if (ret[0] != null)
				{
					cir.setReturnValue(ret[0]);
				}
			}
		}
	}
}
