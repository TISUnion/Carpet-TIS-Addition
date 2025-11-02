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

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class IdentifierUtils
{
	public static ResourceLocation ofVanilla(String id)
	{
		//#if MC >= 12100
		//$$ return ResourceLocation.parse(id);
		//#else
		return new ResourceLocation(id);
		//#endif
	}

	public static ResourceLocation of(String namespace, String path)
	{
		//#if MC >= 12100
		//$$ return ResourceLocation.fromNamespaceAndPath(namespace, path);
		//#else
		return new ResourceLocation(namespace, path);
		//#endif
	}

	public static ResourceLocation id(Block block)
	{
		return Registry.BLOCK.getKey(block);
	}

	public static ResourceLocation id(Fluid fluid)
	{
		return Registry.FLUID.getKey(fluid);
	}

	public static ResourceLocation id(EntityType<?> entityType)
	{
		return Registry.ENTITY_TYPE.getKey(entityType);
	}

	public static ResourceLocation id(BlockEntityType<?> blockEntityType)
	{
		return Registry.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
	}

	public static ResourceLocation id(PoiType poiType)
	{
		return Registry.POINT_OF_INTEREST_TYPE.getKey(poiType);
	}
}
