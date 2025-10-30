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

package carpettisaddition.helpers.rule.shulkerBoxFullDropBackport;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;

public class ShulkerBoxFullDropBackportHelper
{
	public static void onItemEntityDamagedToDie(ItemEntity itemEntity)
	{
		Item item = itemEntity.getItem().getItem();
		if (!(item instanceof BlockItem))
		{
			return;
		}

		// ref: mc1.17+ net.minecraft.item.BlockItem#onItemEntityDestroyed
		Block block = ((BlockItem)item).getBlock();
		if (block instanceof ShulkerBoxBlock)
		{
			CompoundTag compoundTag = itemEntity.getItem().getTag();
			Level world = itemEntity.level;

			if (compoundTag != null && !world.isClientSide())
			{
				ListTag listTag = compoundTag.getCompound("BlockEntityTag").getList("Items", 10);
				listTag.stream()
						.map(CompoundTag.class::cast)
						.map(ItemStack::of)
						.forEach(stack -> {
							world.addFreshEntity(new ItemEntity(
									world,
									//#if MC >= 11500
									itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
									//#else
									//$$ itemEntity.x, itemEntity.y, itemEntity.z,
									//#endif
									stack
							));
						});
			}
		}
	}
}
