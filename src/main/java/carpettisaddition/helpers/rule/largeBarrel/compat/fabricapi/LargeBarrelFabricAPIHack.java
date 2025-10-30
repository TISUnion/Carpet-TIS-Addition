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

package carpettisaddition.helpers.rule.largeBarrel.compat.fabricapi;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.mixins.rule.largeBarrel.compat.fabricapi.DoubleInventoryAccessor;
import carpettisaddition.utils.ReflectionUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

public class LargeBarrelFabricAPIHack
{
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void largeBarrelLookupLogicInject(Level world, BlockPos pos, BlockState state, BlockEntity blockEntity, Direction direction, CallbackInfoReturnable cir)
	{
		if (CarpetTISAdditionSettings.largeBarrel && blockEntity instanceof BarrelBlockEntity)
		{
			Container largeBarrelInventory = LargeBarrelHelper.getInventory(state, world, pos);
			if (largeBarrelInventory instanceof CompoundContainer)
			{
				DoubleInventoryAccessor accessor = (DoubleInventoryAccessor)largeBarrelInventory;
				Object[] ret = new Object[]{null};

				try
				{
					ReflectionUtils.invoker("net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage", "of").
							ifPresent(invoker -> {
								Object first = invoker.apply(null, new Object[]{accessor.getFirst(), direction});
								Object second = invoker.apply(null, new Object[]{accessor.getSecond(), direction});

								ret[0] = ReflectionUtils.constructor("net.fabricmc.fabric.api.transfer.v1.storage.base.CombinedStorage", List.class).
										map(ctr -> ctr.apply(new Object[]{Lists.newArrayList(first, second)})).
										orElse(null);
							});
				}
				catch (ReflectionUtils.InvocationException ignored)
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
