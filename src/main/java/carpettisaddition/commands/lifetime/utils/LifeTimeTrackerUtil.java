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

import carpettisaddition.utils.IdentifierUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.core.Registry;

import java.util.Optional;
import java.util.stream.Stream;

public class LifeTimeTrackerUtil
{
	public static boolean isTrackedEntityClass(Entity entity)
	{
		return entity instanceof Mob || entity instanceof ItemEntity || entity instanceof ExperienceOrb || entity instanceof AbstractMinecart || entity instanceof Boat;
	}

	public static String getEntityTypeDescriptor(EntityType<?> entityType)
	{
		return IdentifierUtils.id(entityType).getPath();
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
