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

package carpettisaddition.utils.compat;

import carpettisaddition.utils.EntityUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.Objects;

//#if MC >= 11600
//$$ import net.minecraft.resources.ResourceKey;
//#else
import net.minecraft.world.level.dimension.DimensionType;
//#endif

/*
 * A wrapper class to deal with dimension type class differences between minecraft version:
 * - DimensionType in 1.15-
 * - ResourceKey<Level> in 1.16+
 */
public class DimensionWrapper
{
	public static final DimensionWrapper OVERWORLD = of(
			//#if MC >= 11600
			//$$ Level.OVERWORLD
			//#else
			DimensionType.OVERWORLD
			//#endif
	);
	public static final DimensionWrapper THE_NETHER = of(
			//#if MC >= 11600
			//$$ Level.NETHER
			//#else
			DimensionType.NETHER
			//#endif
	);
	public static final DimensionWrapper THE_END = of(
			//#if MC >= 11600
			//$$ Level.END
			//#else
			DimensionType.THE_END
			//#endif
	);

	//#if MC >= 11600
	//$$ private final ResourceKey<Level>
	//#else
	private final DimensionType
	//#endif
			dimensionType;

	private DimensionWrapper(
			//#if MC >= 11600
			//$$ ResourceKey<Level> dimensionType
			//#else
			DimensionType dimensionType
			//#endif
	)
	{
		this.dimensionType = dimensionType;
	}

	/**
	 * Warning: mc version dependent
	 */
	public static DimensionWrapper of(
			//#if MC >= 11600
			//$$ ResourceKey<Level> dimensionType
			//#else
			DimensionType dimensionType
			//#endif
	)
	{
		return new DimensionWrapper(dimensionType);
	}

	public static DimensionWrapper of(Level world)
	{
		return new DimensionWrapper(
				//#if MC >= 11600
				//$$ world.dimension()
				//#else
				world.getDimension().getType()
				//#endif
		);
	}

	public static DimensionWrapper of(Entity entity)
	{
		return of(EntityUtils.getEntityWorld(entity));
	}

	/**
	 * Warning: mc version dependent
	 */
	//#if MC >= 11600
	//$$ public ResourceKey<Level> getValue()
	//#else
	public DimensionType getValue()
	//#endif
	{
		return this.dimensionType;
	}

	public ResourceLocation getIdentifier()
	{
		//#if MC >= 11600
		//$$ return this.dimensionType.location();
		//#else
		return DimensionType.getName(this.dimensionType);
		//#endif
	}

	public String getIdentifierString()
	{
		//#if MC >= 11600
		//$$ return this.getIdentifier().toString();
		//#else
		return this.dimensionType.toString();
		//#endif
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DimensionWrapper that = (DimensionWrapper) o;
		return Objects.equals(dimensionType, that.dimensionType);
	}

	@Override
	public int hashCode()
	{
		return this.dimensionType.hashCode();
	}

	@Deprecated
	@Override
	public String toString()
	{
		return this.getIdentifierString();
	}
}
