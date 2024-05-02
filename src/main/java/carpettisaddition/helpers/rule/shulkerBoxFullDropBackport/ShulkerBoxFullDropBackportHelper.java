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

import net.minecraft.block.Block;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.World;

public class ShulkerBoxFullDropBackportHelper
{
	public static void onItemEntityDamagedToDie(ItemEntity itemEntity)
	{
		Item item = itemEntity.getStack().getItem();
		if (!(item instanceof BlockItem))
		{
			return;
		}

		// ref: mc1.17+ net.minecraft.item.BlockItem#onItemEntityDestroyed
		Block block = ((BlockItem)item).getBlock();
		if (block instanceof ShulkerBoxBlock)
		{
			CompoundTag compoundTag = itemEntity.getStack().getTag();
			World world = itemEntity.world;

			if (compoundTag != null && !world.isClient)
			{
				ListTag listTag = compoundTag.getCompound("BlockEntityTag").getList("Items", 10);
				listTag.stream()
						.map(CompoundTag.class::cast)
						.map(ItemStack::fromTag)
						.forEach(stack -> {
							world.spawnEntity(new ItemEntity(
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
