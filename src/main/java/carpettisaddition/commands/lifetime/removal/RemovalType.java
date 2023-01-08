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

import java.util.function.Supplier;

public enum RemovalType
{
	/**
	 * The mob is removed from the world and no longer exists
	 */
	REMOVED_FROM_WORLD(() -> true),
	/**
	 * The mob is removed from the mobcap, but still exists in the world
	 */
	REMOVED_FROM_MOBCAP(() -> CarpetTISAdditionSettings.lifeTimeTrackerConsidersMobcap);

	private final Supplier<Boolean> validSupplier;

	RemovalType(Supplier<Boolean> validSupplier)
	{
		this.validSupplier = validSupplier;
	}

	public boolean isValid()
	{
		return this.validSupplier.get();
	}
}
