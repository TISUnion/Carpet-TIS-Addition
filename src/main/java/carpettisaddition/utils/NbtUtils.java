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

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class NbtUtils
{
	// ============= Element Accessors =============

	public static String getStringOrEmpty(CompoundTag nbt, String key)
	{
		//#if MC >= 12105
		//$$ return nbt.getString(key, "");
		//#else
		return nbt.getString(key);
		//#endif
	}

	public static int getIntOrZero(CompoundTag nbt, String key)
	{
		//#if MC >= 12105
		//$$ return nbt.getInt(key, 0);
		//#else
		return nbt.getInt(key);
		//#endif
	}

	public static long getLongOrZero(CompoundTag nbt, String key)
	{
		//#if MC >= 12105
		//$$ return nbt.getLong(key, 0);
		//#else
		return nbt.getLong(key);
		//#endif
	}

	public static CompoundTag getNbtOrEmpty(CompoundTag nbt, String key)
	{
		//#if MC >= 12105
		//$$ return nbt.getCompound(key).orElseGet(NbtCompound::new);
		//#else
		return nbt.getCompound(key);
		//#endif
	}

	// ============= Conversion Utils =============

	public static CompoundTag stringList2Nbt(List<String> list)
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("length", list.size());
		for (int i = 0; i < list.size(); i++)
		{
			nbt.putString(String.valueOf(i), list.get(i));
		}
		return nbt;
	}

	public static List<String> nbt2StringList(CompoundTag nbt)
	{
		List<String> list = Lists.newArrayList();
		int length = getIntOrZero(nbt, "length");
		for (int i = 0; i < length; i++)
		{
			list.add(getStringOrEmpty(nbt, String.valueOf(i)));
		}
		return list;
	}
}
