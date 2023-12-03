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

package carpettisaddition.utils.deobfuscator;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.deobfuscator.mapping.MappingReader;
import carpettisaddition.utils.deobfuscator.mapping.TinyMappingV2Reader;
import carpettisaddition.utils.deobfuscator.yarn.OnlineMappingProvider;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class StackTraceDeobfuscator
{
	static Map<String, String> MAPPING = Maps.newHashMap();
	static boolean fetchedMapping = false;
	static String MAPPING_VERSION = "no mapping";
	static final Translator translator = new Translator("util.stack_trace");

	public static synchronized void fetchMapping()
	{
		if (!fetchedMapping)
		{
			OnlineMappingProvider.getMapping();
			fetchedMapping = true;
		}
	}

	public static boolean loadMappings(InputStream inputStream, String mappingVersion)
	{
		Map<String, String> mappings;
		MappingReader mappingReader = new TinyMappingV2Reader();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream)))
		{
			mappings = mappingReader.readMapping(reader);
		}
		catch (Exception e)
		{
			CarpetTISAdditionServer.LOGGER.error("Fail to load mapping {}", mappingVersion, e);
			return false;
		}

		MAPPING = mappings;
		MAPPING_VERSION = mappingVersion;
		return true;
	}

	public static StackTraceElement[] deobfuscateStackTrace(StackTraceElement[] stackTraceElements, @Nullable String ignoreClassPath)
	{
		List<StackTraceElement> list = Lists.newArrayList();
		for (StackTraceElement element : stackTraceElements)
		{
			String remappedClass = MAPPING.get(element.getClassName());
			String remappedMethod = MAPPING.get(element.getMethodName());
			StackTraceElement newElement = new StackTraceElement(
					remappedClass != null ? remappedClass : element.getClassName(),
					remappedMethod != null ? remappedMethod : element.getMethodName(),
					remappedClass != null ? getFileName(remappedClass) : element.getFileName(),
					element.getLineNumber()
			);
			list.add(newElement);
			if (ignoreClassPath != null && newElement.getClassName().startsWith(ignoreClassPath))
			{
				list.clear();
			}
		}
		return list.toArray(new StackTraceElement[0]);
	}

	public static StackTraceElement[] deobfuscateStackTrace(StackTraceElement[] stackTraceElements)
	{
		return deobfuscateStackTrace(stackTraceElements, null);
	}

	private static String getFileName(String className)
	{
		if (className.isEmpty())
		{
			return className;
		}
		return className.substring(className.lastIndexOf('.') + 1).split("\\$",2)[0] + ".java";
	}
}
