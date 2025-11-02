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

package carpettisaddition.utils.deobfuscator.yarn;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.FileUtils;
import carpettisaddition.utils.MiscUtils;
import carpettisaddition.utils.deobfuscator.StackTraceDeobfuscator;
import com.google.common.collect.Lists;
import com.google.common.net.UrlEscapers;
import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.*;
import java.nio.file.FileSystem;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OnlineMappingProvider
{
	private static final Logger LOGGER = CarpetTISAdditionServer.LOGGER;
	public static final String MINECRAFT_VERSION = Util.make(() -> {
		//#if MC >= 1.17.1
		//$$ SharedConstants.tryDetectVersion();
		//#endif
		return SharedConstants.getCurrentVersion().getId();
	});
	public static final String YARN_META_URL = "https://meta.fabricmc.net/v2/versions/yarn/" + MINECRAFT_VERSION;
	public static final String YARN_MAPPING_URL_BASE = "https://maven.fabricmc.net/net/fabricmc/yarn/";
	public static final String MAPPINGS_JAR_LOCATION = "mappings/mappings.tiny";
	public static final String STORAGE_DIRECTORY = String.format("./config/%s/mapping/", CarpetTISAdditionServer.compactName);
	public static final String YARN_VERSION_CACHE_FILE = STORAGE_DIRECTORY + "yarn_version.json";

	private static String getMappingFileName(String yarnVersion)
	{
		return String.format("yarn-%s-v2", yarnVersion);
	}

	private static String getMappingFileNameFull(String yarnVersion)
	{
		return getMappingFileName(yarnVersion) + ".tiny";
	}

	private static String getYarnVersionOnline() throws IOException
	{
		URL url = URI.create(YARN_META_URL).toURL();
		URLConnection request = url.openConnection();
		List<Pair<Integer, String>> list = Lists.newArrayList();
		JsonElement json =
				//#if MC >= 11800
				//$$ JsonParser.parseReader
				//#else
				(new JsonParser()).parse
				//#endif
						(new InputStreamReader(request.getInputStream()));
		json.getAsJsonArray().forEach(e -> {
			JsonObject object = e.getAsJsonObject();
			list.add(Pair.of(object.get("build").getAsInt(), object.get("version").getAsString()));
		});
		return list.stream().max(Comparator.comparingInt(Pair::getFirst)).orElseThrow(() -> new IOException("Empty list")).getSecond();
	}

	synchronized private static String getYarnVersion(boolean useCache) throws IOException
	{
		List<YarnVersionCache> cacheList = Lists.newArrayList();

		// read
		File file = new File(YARN_VERSION_CACHE_FILE);
		if (FileUtils.isFile(file))
		{
			YarnVersionCache[] caches = null;
			try
			{
				caches = new Gson().fromJson(new InputStreamReader(Files.newInputStream(file.toPath())), YarnVersionCache[].class);
			}
			catch (Exception e)
			{
				CarpetTISAdditionMod.LOGGER.warn("Failed to deserialize data from {}: {}", YARN_VERSION_CACHE_FILE, e);
			}
			if (caches != null)
			{
				cacheList.addAll(Arrays.asList(caches));
			}
		}

		// scan
		YarnVersionCache storedCache = null;
		for (YarnVersionCache cache : cacheList)
		{
			if (cache.minecraftVersion.equals(OnlineMappingProvider.MINECRAFT_VERSION))
			{
				storedCache = cache;
				break;
			}
		}
		if (useCache && storedCache != null)
		{
			LOGGER.debug("Found yarn version from file cache");
			return storedCache.yarnVersion;
		}

		// download
		String yarnVersion = getYarnVersionOnline();
		cacheList.remove(storedCache);
		cacheList.add(new YarnVersionCache(OnlineMappingProvider.MINECRAFT_VERSION, yarnVersion));

		// store
		FileUtils.touchFileDirectory(file);
		OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(file.toPath()));
		writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(cacheList));
		writer.flush();
		writer.close();

		return yarnVersion;
	}

	synchronized private static FileInputStream getYarnMappingStream(String yarnVersion) throws IOException
	{
		File mappingFile = new File(STORAGE_DIRECTORY + getMappingFileNameFull(yarnVersion));
		if (!FileUtils.isFile(mappingFile))
		{
			String mappingJar = String.format("%s.jar", getMappingFileName(yarnVersion));
			String mappingJarUrl = String.format("%s%s/%s", YARN_MAPPING_URL_BASE, yarnVersion, mappingJar);
			String escapedUrl = UrlEscapers.urlFragmentEscaper().escape(mappingJarUrl);

			LOGGER.info("Downloading yarn mapping from {}", escapedUrl);
			File jarFile = new File(STORAGE_DIRECTORY + mappingJar);
			org.apache.commons.io.FileUtils.copyURLToFile(URI.create(escapedUrl).toURL(), jarFile);

			try (FileSystem jar = FileSystems.newFileSystem(jarFile.toPath(), (ClassLoader) null))
			{
				Files.copy(jar.getPath(MAPPINGS_JAR_LOCATION), mappingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
 			}
			Files.delete(jarFile.toPath());
		}
		return new FileInputStream(mappingFile);
	}

	synchronized private static void loadMappings(InputStream mappingStream, String yarnVersion)
	{
		if (StackTraceDeobfuscator.loadMappings(mappingStream, "Yarn " + yarnVersion))
		{
			LOGGER.info("Yarn mapping file {} loaded", getMappingFileNameFull(yarnVersion));
		}
	}

	private static void getMappingThreaded()
	{
		try
		{
			// 0. Migrate
			moveFilesIntoMappingDir();

			// 1. Get yarn version
			String yarnVersion = getYarnVersion(true);
			LOGGER.debug("Got yarn version for Minecraft {}: {}", MINECRAFT_VERSION, yarnVersion);

			// 2. Get yarn mapping
			FileInputStream mappingStream = getYarnMappingStream(yarnVersion);

			// 3. Load yarn mapping
			loadMappings(mappingStream, yarnVersion);

			// 4. Schedule yarn version updating
//			MiscUtils.startThread("Yarn Version Updater", () -> checkYarnVersionUpdate(yarnVersion));
		}
		catch (IOException e)
		{
			LOGGER.error("Failed to get yarn mapping, the stack trace deobfuscator will not work: {}", e.toString());
		}
	}

	@SuppressWarnings("unused")
	private static void checkYarnVersionUpdate(String currentYarnVersion)
	{
		try
		{
			String newVersion = getYarnVersion(false);
			if (!currentYarnVersion.equals(newVersion))
			{
				LOGGER.info("New yarn version detected: {} -> {}", currentYarnVersion, newVersion);
				FileInputStream mappingStream = getYarnMappingStream(newVersion);
				loadMappings(mappingStream, newVersion);

				// yeets old mapping
				File oldMapping = new File(STORAGE_DIRECTORY + getMappingFileNameFull(currentYarnVersion));
				Files.delete(oldMapping.toPath());
			}
		}
		catch (IOException e)
		{
			LOGGER.error("Check yarn version update failed: {}", e.toString());
		}
	}

	/**
	 * A migration method for <= v1.44
	 * Moved mapping stuffs at config root to a mapping folder
	 */
	private static void moveFilesIntoMappingDir() throws IOException
	{
		final String MAPPING_STORAGE_DIRECTORY_OLD = String.format("./config/%s/", CarpetTISAdditionServer.compactName);
		final String MAPPING_CONFIG = "yarn_version.json";
		final String MAPPING_SUFFIX = "-v2.tiny";

		List<File> targets = null;
		try (Stream<Path> paths = Files.list(new File(MAPPING_STORAGE_DIRECTORY_OLD).toPath()))
		{
			targets = paths.map(Path::toFile).
					filter(File::isFile).
					filter(file -> file.getName().equals(MAPPING_CONFIG) || file.getName().endsWith(MAPPING_SUFFIX)).
					collect(Collectors.toList());
		}
		catch (NoSuchFileException ignored)
		{
		}

		if (targets != null && !targets.isEmpty())
		{
			FileUtils.touchDirectory(new File(STORAGE_DIRECTORY));
			for (File file : targets)
			{
				Files.move(file.toPath(), new File(STORAGE_DIRECTORY).toPath().resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

	/**
	 * Entry point
	 */
	public static void getMapping()
	{
		MiscUtils.startThread("TISCM Mapping", OnlineMappingProvider::getMappingThreaded);
	}
}
