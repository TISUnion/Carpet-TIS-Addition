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

package carpettisaddition.helpers.rule.tooledTNT;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion;

import java.util.Optional;

public class TooledTNTHelper
{
	/**
	 * Attach the causing entity's main hand item onto the loot table builder
	 *
	 * See also {@link net.minecraft.block.Block#getDroppedStacks(BlockState, ServerWorld, BlockPos, BlockEntity, Entity, ItemStack)}
	 * for loot table building for regular entity block mining
	 */
	public static Optional<ItemStack> getMainHandItemOfCausingEntity(Explosion explosion)
	{
		if (CarpetTISAdditionSettings.tooledTNT)
		{
			LivingEntity causingEntity = explosion.getCausingEntity();
			if (causingEntity != null)
			{
				return Optional.of(causingEntity.getMainHandStack());
			}
		}
		// vanilla
		return Optional.empty();
	}
}
