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

package carpettisaddition.logging.loggers.microtiming.marker;

import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class StorageKey
{
	private final DimensionWrapper dimensionType;
	private final BlockPos blockPos;

	private StorageKey(DimensionWrapper dimensionType, BlockPos blockPos)
	{
		this.dimensionType = dimensionType;
		this.blockPos = blockPos;
	}

	public StorageKey(World world, BlockPos blockPos)
	{
		this(DimensionWrapper.of(world), blockPos);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StorageKey that = (StorageKey) o;
		return Objects.equals(dimensionType, that.dimensionType) && Objects.equals(blockPos, that.blockPos);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(dimensionType, blockPos);
	}
}
