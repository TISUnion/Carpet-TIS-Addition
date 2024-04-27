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

package carpettisaddition.helpers.rule.optimizedHardHitBoxEntityCollision;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;

public class OptimizedHardHitBoxEntityCollisionHelper
{
	public static final ThreadLocal<Boolean> checkHardHitBoxEntityOnly = ThreadLocal.withInitial(() -> false);

	/**
	 * (<=1.15) Check if the given entity may have {@link Entity#getHardCollisionBox} return not null
	 * (>=1.16) Check if the given entity may have {@link Entity#collidesWith} return true if other entity has {@link Entity#isCollidable} returns false
	 * In vanilla only boat and minecart do that
	 *
	 * Doesn't work with other mods that add new type of entities
	 */
	public static boolean treatsGeneralEntityAsHardHitBox(Entity entity)
	{
		return entity instanceof BoatEntity || entity instanceof AbstractMinecartEntity;
	}

	/**
	 * Check if the given entity may have hard hitbox
	 * In vanilla only boat and shulker have hard hitbox
	 * For boat, Entity#getCollisionBox always returns not null
	 * For shulker, Entity#getCollisionBox returns not null if the shulker is alive, in case of edge cases
	 * here comes an extra shulker check
	 *
	 * Might not work with other mods since they might have dynamic Entity#getCollisionBox result
	 *
	 * ----- ^ doc for <= 1.15.2 -----
	 * In 1.16 there's a method called {@link Entity#isCollidable}, which handles this logic well, so just use it
	 */
	public static boolean hasHardHitBox(Entity entity)
	{
		//#if MC >= 11600
		return entity.isCollidable();
		//#else
		//$$ return entity.getCollisionBox() != null || entity instanceof ShulkerEntity;
		//#endif
	}
}
