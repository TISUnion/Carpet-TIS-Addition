/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.commands.spawn.natualSpawning;

import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

class NatualSpawningUint
{
	public final DimensionWrapper dimension;
	public final MobCategory catalogue;

	public NatualSpawningUint(DimensionWrapper dimension, MobCategory catalogue)
	{
		this.dimension = dimension;
		this.catalogue = catalogue;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || getClass() != o.getClass()) return false;
		NatualSpawningUint that = (NatualSpawningUint)o;
		return Objects.equals(dimension, that.dimension) && catalogue == that.catalogue;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(dimension, catalogue);
	}
}
