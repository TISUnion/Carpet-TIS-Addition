/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.utils.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.world.phys.Vec3;

import java.io.IOException;

public class Vec3dAdaptor extends TypeAdapter<Vec3>
{
	@Override
	public void write(JsonWriter writer, Vec3 value) throws IOException
	{
		if (value == null)
		{
			writer.nullValue();
			return;
		}
		writer.beginArray();
		writer.value(value.x);
		writer.value(value.y);
		writer.value(value.z);
		writer.endArray();
	}

	@Override
	public Vec3 read(JsonReader reader) throws IOException
	{
		throw new UnsupportedOperationException("Not supported yet");
	}
}
