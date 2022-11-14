package carpettisaddition.translations;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.FileUtil;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;

import java.util.*;

import static carpettisaddition.translations.TranslationConstants.TRANSLATION_NAMESPACE;

public class TranslationLoader
{
	private static final String RESOURCE_DIR = String.format("assets/%s/lang", TRANSLATION_NAMESPACE);

	public static void loadTranslations(Map<String, Map<String, String>> translationStorage)
	{
		List<String> languageList;

		try
		{
			String fileContent = FileUtil.readResourceFileAsString(RESOURCE_DIR + "/meta/languages.json");
			languageList = new Gson().fromJson(fileContent, LanguageList.class);
		}
		catch (Exception e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to read language list", e);
			return;
		}

		languageList.forEach(lang ->
				translationStorage.computeIfAbsent(lang, TranslationLoader::loadTranslationFile)
		);
	}

	private static Map<String, String> loadTranslationFile(String lang)
	{
		try
		{
			String fileContent = FileUtil.readResourceFileAsString(String.format("%s/%s.json", RESOURCE_DIR, lang));
			return new Gson().fromJson(fileContent, TranslationMapping.class);
		}
		catch (Exception e)
		{
			String message = "Failed to load translation of language " + lang;
			CarpetTISAdditionServer.LOGGER.error(message, e);
			if (FabricLoader.getInstance().isDevelopmentEnvironment())
			{
				throw new RuntimeException(message, e);
			}
			return Collections.emptyMap();
		}
	}

	private static class LanguageList extends ArrayList<String>
	{
	}

	private static class TranslationMapping extends LinkedHashMap<String, String>
	{
	}
}
