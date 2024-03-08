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

package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.utils.IdentifierUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.registry.Registry;

import java.util.Optional;
import java.util.stream.Stream;

public class LifeTimeTrackerUtil
{
	public static boolean isTrackedEntityClass(Entity entity)
	{
		return entity instanceof MobEntity || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof AbstractMinecartEntity || entity instanceof BoatEntity;
	}

	public static String getEntityTypeDescriptor(EntityType<?> entityType)
	{
		return IdentifierUtil.id(entityType).getPath();
	}

	public static Optional<EntityType<?>> getEntityTypeFromName(String name)
	{
		return Registry.ENTITY_TYPE.stream().filter(entityType -> getEntityTypeDescriptor(entityType).equals(name)).findFirst();
	}

	public static Stream<String> getEntityTypeDescriptorStream()
	{
		return Registry.ENTITY_TYPE.stream().map(LifeTimeTrackerUtil::getEntityTypeDescriptor);
	}
}
