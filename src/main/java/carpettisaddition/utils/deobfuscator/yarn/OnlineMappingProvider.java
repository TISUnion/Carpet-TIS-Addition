package carpettisaddition.utils.deobfuscator.yarn;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.FileUtil;
import carpettisaddition.utils.MiscUtil;
import carpettisaddition.utils.deobfuscator.StackTraceDeobfuscator;
import com.google.common.collect.Lists;
import com.google.common.net.UrlEscapers;
import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import net.minecraft.MinecraftVersion;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class OnlineMappingProvider
{
	private static final Logger LOGGER = CarpetTISAdditionServer.LOGGER;
	public static final String MINECRAFT_VERSION =
			//#if MC >= 11700
			//$$ // Minecraft's version "1.18-experimental-4" is not the same as yarn format, so the version is handled trickily
			//$$ MinecraftVersion.GAME_VERSION.getName().replace("-experimental-", "_experimental-snapshot-");
			//#elseif MC >= 11600
			//$$ MinecraftVersion.field_25319.getName();
			//#else
			new MinecraftVersion().getName();
			//#endif
	public static final String YARN_META_URL = "https://meta.fabricmc.net/v2/versions/yarn/" + MINECRAFT_VERSION;
	public static final String YARN_MAPPING_URL_BASE = "https://maven.fabricmc.net/net/fabricmc/yarn/";
	public static final String MAPPINGS_JAR_LOCATION = "mappings/mappings.tiny";
	public static final String STORAGE_DIRECTORY = String.format("./config/%s/", CarpetTISAdditionServer.compactName);
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
		URL url = new URL(YARN_META_URL);
		URLConnection request = url.openConnection();
		List<Pair<Integer, String>> list = Lists.newArrayList();
		JsonElement json = (new JsonParser()).parse(new InputStreamReader(request.getInputStream()));
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
		if (FileUtil.isFile(file))
		{
			YarnVersionCache[] caches = new Gson().fromJson(new InputStreamReader(new FileInputStream(file)), YarnVersionCache[].class);
			cacheList.addAll(Arrays.asList(caches));
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
		FileUtil.prepareFileDirectories(file);
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
		writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(cacheList));
		writer.flush();
		writer.close();

		return yarnVersion;
	}

	synchronized private static FileInputStream getYarnMappingStream(String yarnVersion) throws IOException
	{
		File mappingFile = new File(STORAGE_DIRECTORY + getMappingFileNameFull(yarnVersion));
		if (!FileUtil.isFile(mappingFile))
		{
			String mappingJar = String.format("%s.jar", getMappingFileName(yarnVersion));
			String mappingJarUrl = String.format("%s%s/%s", YARN_MAPPING_URL_BASE, yarnVersion, mappingJar);
			String escapedUrl = UrlEscapers.urlFragmentEscaper().escape(mappingJarUrl);

			LOGGER.info("Downloading yarn mapping from {}", escapedUrl);
			File jarFile = new File(STORAGE_DIRECTORY + mappingJar);
			FileUtils.copyURLToFile(new URL(escapedUrl), jarFile);

			FileSystem jar = FileSystems.newFileSystem(jarFile.toPath(), (ClassLoader)null);
			Files.copy(jar.getPath(MAPPINGS_JAR_LOCATION), mappingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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

	private static void getMappingInner()
	{
		try
		{
			// 1. Get yarn version
			String yarnVersion = getYarnVersion(true);
			LOGGER.debug("Got yarn version for Minecraft {}: {}", MINECRAFT_VERSION, yarnVersion);

			// 2. Get yarn mapping
			FileInputStream mappingStream = getYarnMappingStream(yarnVersion);

			// 3. Load yarn mapping
			loadMappings(mappingStream, yarnVersion);

			// 4. Schedule yarn version updating
//			MiscUtil.startThread("Yarn Version Updater", () -> checkYarnVersionUpdate(yarnVersion));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

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
			e.printStackTrace();
		}
	}

	/**
	 * Entry point
	 */
	public static void getMapping()
	{
		MiscUtil.startThread("TISCM Mapping", OnlineMappingProvider::getMappingInner);
	}
}
