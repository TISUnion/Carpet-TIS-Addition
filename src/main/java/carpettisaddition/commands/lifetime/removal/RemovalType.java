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

package carpettisaddition.commands.lifetime.removal;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.function.Supplier;

public enum RemovalType
{
	/**
	 * The mob is removed from the world and no longer exists
	 */
	REMOVED_FROM_WORLD(entity -> true),
	/**
	 * The mob is removed from the mobcap, but still exists in the world
	 */
	REMOVED_FROM_MOBCAP(entity -> {
		// only MobEntity will be counted in mobcap
		return CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap && entity instanceof Mob;
	});

	private final Validator validSupplier;

	RemovalType(Validator validSupplier)
	{
		this.validSupplier = validSupplier;
	}

	public boolean isValid(Entity entity)
	{
		return this.validSupplier.isValidFor(entity);
	}

	interface Validator
	{
		boolean isValidFor(Entity entity);
	}
}
