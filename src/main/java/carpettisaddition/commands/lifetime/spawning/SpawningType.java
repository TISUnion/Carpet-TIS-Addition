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

package carpettisaddition.commands.lifetime.spawning;

import carpettisaddition.CarpetTISAdditionSettings;

import java.util.function.Supplier;

public enum SpawningType
{
	/**
	 * The mob is newly added to the world for the first time
	 */
	ADDED_TO_WORLD(() -> false),
	/**
	 * The mob is already in the world, and now it's added to the mobcap
	 */
	ADDED_TO_MOBCAP(() -> CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap);

	private final Supplier<Boolean> validSupplier;

	SpawningType(Supplier<Boolean> validSupplier)
	{
		this.validSupplier = validSupplier;
	}

	public boolean isDoubleRecordSupported()
	{
		return this.validSupplier.get();
	}
}
