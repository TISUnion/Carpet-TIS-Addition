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

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil
{
	public static void touchFileDirectory(File file) throws IOException
	{
		touchDirectory(file.getParentFile());
	}

	public static void touchDirectory(File dir) throws IOException
	{
		if (!dir.exists())
		{
			if (!dir.mkdirs())
			{
				throw new IOException("Directory creation failed");
			}
		}
		else if (!dir.isDirectory())
		{
			throw new IOException("Directory exists but it's not a directory");
		}
	}

	public static boolean isFile(File file)
	{
		return file.exists() && file.isFile();
	}

	public static boolean isFile(String fileName)
	{
		return isFile(new File(fileName));
	}

	public static String readResourceFileAsString(String path) throws IOException
	{
		InputStream inputStream = FileUtil.class.getClassLoader().getResourceAsStream(path);
		if (inputStream == null)
		{
			throw new IOException("Null input stream from path " + path);
		}
		return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
	}
}
