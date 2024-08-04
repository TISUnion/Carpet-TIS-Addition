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

package carpettisaddition.utils;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

public class IdentifierUtil
{
	public static Identifier ofVanilla(String id)
	{
		//#if MC >= 12100
		//$$ return Identifier.of(id);
		//#else
		return new Identifier(id);
		//#endif
	}

	public static Identifier of(String namespace, String path)
	{
		//#if MC >= 12100
		//$$ return Identifier.of(namespace, path);
		//#else
		return new Identifier(namespace, path);
		//#endif
	}

	public static Identifier id(Block block)
	{
		return Registry.BLOCK.getId(block);
	}

	public static Identifier id(Fluid fluid)
	{
		return Registry.FLUID.getId(fluid);
	}

	public static Identifier id(EntityType<?> entityType)
	{
		return Registry.ENTITY_TYPE.getId(entityType);
	}

	public static Identifier id(BlockEntityType<?> blockEntityType)
	{
		return Registry.BLOCK_ENTITY_TYPE.getId(blockEntityType);
	}

	public static Identifier id(PointOfInterestType poiType)
	{
		return Registry.POINT_OF_INTEREST_TYPE.getId(poiType);
	}
}
