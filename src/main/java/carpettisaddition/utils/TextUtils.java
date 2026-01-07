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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;

/**
 * Minecraft related stuffs -> String
 */
public class TextUtils
{
	public static String tp(Vec3 pos) {return String.format("/tp %s %s %s", StringUtils.dts(pos.x()), StringUtils.dts(pos.y()), StringUtils.dts(pos.z()));}
	public static String tp(Vec3i pos) {return String.format("/tp %d %d %d", pos.getX(), pos.getY(), pos.getZ());}
	public static String tp(ChunkPos pos) {return String.format("/tp %d ~ %d", PositionUtils.chunkPosX(pos) * 16 + 8, PositionUtils.chunkPosX(pos) * 16 + 8);}
	public static String tp(Vec3 pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}
	public static String tp(Vec3i pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}
	public static String tp(ChunkPos pos, DimensionWrapper dimensionType) {return String.format("/execute in %s run", dimensionType) + tp(pos).replace('/', ' ');}

	public static String tp(Entity entity)
	{
		if (entity instanceof Player)
		{
			String name = ((Player)entity).getGameProfile().getName();
			return String.format("/tp %s", name);
		}
		String uuid = entity.getUUID().toString();
		return String.format("/tp %s", uuid);
	}

	public static String coord(Vec3 pos) {return String.format("[%.1f, %.1f, %.1f]", pos.x(), pos.y(), pos.z());}
	public static String coord(Vec3i pos) {return String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());}
	public static String coord(ChunkPos pos) {return String.format("[%d, %d]", PositionUtils.chunkPosX(pos), PositionUtils.chunkPosX(pos));}

	public static String vector(Vec3 vec, int digits)
	{
		return String.format("(%s, %s, %s)", StringUtils.fractionDigit(vec.x(), digits), StringUtils.fractionDigit(vec.y(), digits), StringUtils.fractionDigit(vec.z(), digits));
	}
	public static String vector(Vec3 vec) {return vector(vec, 2);}

	public static String block(Block block)
	{
		return IdentifierUtils.id(block).toString();
	}

	public static String block(BlockState blockState)
	{
		return BlockStateParser.serialize(blockState);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<T>> String property(Property<T> property, Object value)
	{
		return property.getName((T)value);
	}

	public static String byteSizeSi(long size)
	{
		return byteSizeSi(size, 1);
	}

	public static String byteSizeSi(long size, int digit)
	{
		if (size == Long.MIN_VALUE)
		{
			// byteSizeSi is not a 100% precise function anyway, XD
			size = Long.MIN_VALUE + 1;
		}
		if (size < 0)
		{
			return "-" + byteSizeSi(-size);
		}

		if (size < 1024)
		{
			return size + "B";
		}
		else if (size < 1024 * 1024)
		{
			return StringUtils.fractionDigit(size / 1024.0, digit) + "KiB";
		}
		else if (size < 1024L * 1024 * 1024)
		{
			return StringUtils.fractionDigit(size / 1024.0 / 1024.0, digit) + "MiB";
		}
		else if (size < 1024L * 1024 * 1024 * 1024)
		{
			return StringUtils.fractionDigit(size / 1024.0 / 1024.0 / 1024.0, digit) + "GiB";
		}
		else
		{
			return StringUtils.fractionDigit(size / 1024.0 / 1024.0 / 1024.0 / 1024.0, digit) + "TiB";
		}
	}
}
