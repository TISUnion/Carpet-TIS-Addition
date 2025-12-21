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

package carpettisaddition.utils.deobfuscator.mapping;

import com.google.common.collect.Maps;
import net.fabricmc.mapping.reader.v2.MappingGetter;
import net.fabricmc.mapping.reader.v2.TinyMetadata;
import net.fabricmc.mapping.reader.v2.TinyV2Factory;
import net.fabricmc.mapping.reader.v2.TinyVisitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * Implemented in obfuscated version only (cuz that's where yarn and intermediary mapping exists).
 * In unobfuscated versions, this reader reads nothing
 */
public class TinyMappingV2Reader implements MappingReader
{
	@Override
	public Map<String, String> readMapping(BufferedReader reader) throws IOException
	{
		Map<String, String> mappings = Maps.newHashMap();
		TinyV2Factory.visit(reader, new MappingVisitor(mappings));
		return mappings;
	}

	private static class MappingVisitor implements TinyVisitor
	{
		private final Map<String, String> mappings;
		private int intermediaryIndex;
		private int namedIndex;

		public MappingVisitor(Map<String, String> mappings)
		{
			this.mappings = mappings;
		}

		private void putMappings(MappingGetter name)
		{
			String intermediaryName = name.get(this.intermediaryIndex).replace('/', '.');
			String remappedName = name.get(this.namedIndex).replace('/', '.');
			this.mappings.put(intermediaryName, remappedName);
		}

		@Override
		public void start(TinyMetadata metadata)
		{
			this.intermediaryIndex = metadata.index("intermediary");
			this.namedIndex = metadata.index("named");
		}

		@Override
		public void pushClass(MappingGetter name)
		{
			this.putMappings(name);
		}

		@Override
		public void pushField(MappingGetter name, String descriptor)
		{
			this.putMappings(name);
		}

		@Override
		public void pushMethod(MappingGetter name, String descriptor)
		{
			this.putMappings(name);
		}
	}
}
