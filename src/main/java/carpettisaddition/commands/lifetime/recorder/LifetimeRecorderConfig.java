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

package carpettisaddition.commands.lifetime.recorder;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LifetimeRecorderConfig
{
	private static final Path BASE_DIR = Paths.get("config", CarpetTISAdditionMod.COMPACT_NAME, "lifetime");
	public static final Path CONFIG_FILE_PATH = BASE_DIR.resolve("recorder_config.json");

	public boolean enabled = false;
	public int requiredPermissionLevel = 4;
	public boolean consoleOrSinglePlayerOwnerOnly = true;
	public String outputDirectory = BASE_DIR.resolve("records").toString();
	public long maxOutputRecordCount = -1;
	public long maxOutputFileBytes = 100 * 1024 * 1024;
	public long maxTotalOutputFileCount = 500;
	public long maxTotalOutputFileBytes = 1024 * 1024 * 1024;
	public double sampleRate = 1;

	public static LifetimeRecorderConfig load()
	{
		LifetimeRecorderConfig config = new LifetimeRecorderConfig();
		try
		{
			if (FileUtils.isFile(CONFIG_FILE_PATH.toFile()))
			{
				config = loadNoCheck();
			}
		}
		catch (Exception e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to load lifetime recorder config file", e);
		}

		config.save();
		return config;
	}

	public static LifetimeRecorderConfig loadNoCheck() throws IOException
	{
		try (InputStreamReader isr = new InputStreamReader(Files.newInputStream(CONFIG_FILE_PATH), StandardCharsets.UTF_8))
		{
			return new Gson().fromJson(isr, LifetimeRecorderConfig.class);
		}
	}

	public void save()
	{
		try
		{
			FileUtils.touchDirectory(BASE_DIR);

			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			String content = gson.toJson(this);
			try (BufferedWriter fw = Files.newBufferedWriter(CONFIG_FILE_PATH, StandardCharsets.UTF_8))
			{
				fw.write(content);
			}
		}
		catch (Exception e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to save lifetime recorder config file", e);
		}
	}
}
