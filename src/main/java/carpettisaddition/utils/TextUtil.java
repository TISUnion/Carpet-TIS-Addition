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

import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.command.arguments.BlockArgumentParser;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * Minecraft related stuffs -> String
 */
public class TextUtil
{
	public static String tp(Vec3d pos) {return String.format("/tp %s %s %s", pos.getX(), pos.getY(), pos.getZ());}
	public static String tp(Vec3i pos) {return String.format("/tp %d %d %d", pos.getX(), pos.getY(), pos.getZ());}
	public static String tp(ChunkPos pos) {return String.format("/tp %d ~ %d", pos.x * 16 + 8, pos.z * 16 + 8);}
	public static String tp(Vec3d pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}
	public static String tp(Vec3i pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}
	public static String tp(ChunkPos pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}

	public static String tp(Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			String name = ((PlayerEntity)entity).getGameProfile().getName();
			return String.format("/tp %s", name);
		}
		String uuid = entity.getUuid().toString();
		return String.format("/tp %s", uuid);
	}

	public static String coord(Vec3d pos) {return String.format("[%.1f, %.1f, %.1f]", pos.getX(), pos.getY(), pos.getZ());}
	public static String coord(Vec3i pos) {return String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());}
	public static String coord(ChunkPos pos) {return String.format("[%d, %d]", pos.x, pos.z);}

	public static String vector(Vec3d vec, int digits)
	{
		return String.format("(%s, %s, %s)", StringUtil.fractionDigit(vec.getX(), digits), StringUtil.fractionDigit(vec.getY(), digits), StringUtil.fractionDigit(vec.getZ(), digits));
	}
	public static String vector(Vec3d vec) {return vector(vec, 2);}

	public static String block(Block block)
	{
		return IdentifierUtil.id(block).toString();
	}

	public static String block(BlockState blockState)
	{
		return BlockArgumentParser.stringifyBlockState(blockState);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> String property(Property<T> property, Object value)
	{
		return property.name((T)value);
	}
}
