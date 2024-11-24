/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class PositionUtils
{
	public static BlockPos flooredBlockPos(Vec3d vec3d)
	{
		//#if MC >= 11904
		//$$ return BlockPos.ofFloored(vec3d);
		//#else
		return new BlockPos(vec3d);
		//#endif
	}

	public static BlockBox createBlockBox(BlockPos pos1, BlockPos pos2)
	{
		//#if MC >= 11700
		//$$ return BlockBox.create(pos1, pos2);
		//#else
		return new BlockBox(pos1, pos2);
		//#endif
	}
}
