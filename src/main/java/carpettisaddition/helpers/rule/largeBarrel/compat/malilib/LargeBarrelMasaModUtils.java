/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.largeBarrel.compat.malilib;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.largeBarrel.LargeBarrelHelper;
import carpettisaddition.utils.ReflectionUtils;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class LargeBarrelMasaModUtils
{
	public static Container modifyGetBlockInventoryReturnValue(Container inventory, Level world, BlockPos pos)
	{
		if (CarpetTISAdditionSettings.largeBarrel && inventory instanceof BarrelBlockEntity)
		{
			LargeBarrelHelper.enabledOffThreadBlockEntityAccess.set(true);
			try
			{
				Container largeBarrelInventory = LargeBarrelHelper.getInventory(world.getBlockState(pos), world, pos);
				if (largeBarrelInventory != null)
				{
					inventory = largeBarrelInventory;
				}
			}
			finally
			{
				LargeBarrelHelper.enabledOffThreadBlockEntityAccess.set(false);
			}
		}
		return inventory;
	}

	public static void invokeIDataSyncerRequestBlockEntity(Object ds, Level world, BlockPos pos)
	{
		Predicate<Method> methodPredicate = m -> Arrays.equals(m.getParameterTypes(), new Class<?>[]{Level.class, BlockPos.class});

		Optional<BiFunction<Object, Object[], Object>> invoker;
		invoker = ReflectionUtils.invoker(ds.getClass(), "requestBlockEntity", methodPredicate);
		if (!invoker.isPresent())
		{
			invoker = ReflectionUtils.invoker("fi.dy.masa.malilib.interfaces.IDataSyncer", "requestBlockEntity", methodPredicate);
		}

		if (invoker.isPresent())
		{
			invoker.get().apply(ds, new Object[]{world, pos});
		}
		else
		{
			CarpetTISAdditionMod.LOGGER.warn("Failed to locate method fi.dy.masa.malilib.interfaces.IDataSyncer#getBlockInventory for {} at {}", ds, pos);
		}
	}

	public static Container modifyGetBlockInventoryReturnValueForIDataSyncer(Object ds, Container inventory, Level world, BlockPos pos)
	{
		if (CarpetTISAdditionSettings.largeBarrel && inventory instanceof BarrelBlockEntity)
		{
			BlockPos otherPos = LargeBarrelHelper.getOtherPos(world.getBlockState(pos), world, pos);
			if (otherPos != null)
			{
				// Perform block entity sync for otherPos
				invokeIDataSyncerRequestBlockEntity(ds, world, otherPos);

				return modifyGetBlockInventoryReturnValue(inventory, world, pos);
			}
		}

		return inventory;
	}
}
