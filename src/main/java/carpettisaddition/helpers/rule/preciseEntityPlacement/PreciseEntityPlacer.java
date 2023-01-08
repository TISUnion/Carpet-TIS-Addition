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

package carpettisaddition.helpers.rule.preciseEntityPlacement;

import carpettisaddition.utils.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.math.Vec3d;

public class PreciseEntityPlacer
{
	public static final ThreadLocal<Vec3d> spawnEggTargetPos = ThreadLocal.withInitial(() -> null);

	public static void adjustEntity(Entity entity, Vec3d targetPos)
	{
		entity.refreshPositionAndAngles(targetPos.getX(), targetPos.getY(), targetPos.getZ(), EntityUtil.getYaw(entity), EntityUtil.getPitch(entity));
	}
	public static void adjustEntity(Entity entity, ItemUsageContext context)
	{
		adjustEntity(entity, context.getHitPos());
	}

	/**
	 * The spawnEggTargetPos should be set in SpawnEggItemMixin in advanced
	 */
	public static void adjustEntityFromSpawnEgg(Entity entity)
	{
		Vec3d vec3d = spawnEggTargetPos.get();
		if (vec3d != null)
		{
			adjustEntity(entity, vec3d);
			spawnEggTargetPos.remove();
		}
	}
}
