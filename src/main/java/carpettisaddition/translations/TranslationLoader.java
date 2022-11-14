package carpettisaddition.translations;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.FileUtil;
import com.google.common.collect.Maps;
import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static carpettisaddition.translations.TranslationConstants.TRANSLATION_NAMESPACE;

public class TranslationLoader
{
	private static final String RESOURCE_DIR = String.format("assets/%s/lang", TRANSLATION_NAMESPACE);

	@SuppressWarnings("unchecked")
	public static void loadTranslations(Map<String, Map<String, String>> translationStorage)
	{
		try
		{
			String dataStr = FileUtil.readResourceFileAsString(RESOURCE_DIR + "/meta/languages.yml");
			Map<String, Object> yamlMap = new Yaml().load(dataStr);
			((List<String>)yamlMap.get("languages")).
					forEach(lang ->
							translationStorage.computeIfAbsent(lang, TranslationLoader::loadTranslationFile)
					);
		}
		catch (Exception e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to read language list", e);
		}
	}

	private static Map<String, String> loadTranslationFile(String lang)
	{
		String dataStr;
		try
		{
			dataStr = FileUtil.readResourceFileAsString(String.format("%s/%s.yml", RESOURCE_DIR, lang));
		}
		catch (IOException e)
		{
			return Collections.emptyMap();
		}
		try
		{
			Map<String, Object> yamlMap = new Yaml().load(dataStr);
			Map<String, String> translation = Maps.newLinkedHashMap();
			build(translation, yamlMap, "");
			return translation;
		}
		catch (Exception e)
		{
			CarpetTISAdditionServer.LOGGER.error("Failed to load translation of language " + lang, e);
			if (FabricLoader.getInstance().isDevelopmentEnvironment())
			{
				throw e;
			}
			return Collections.emptyMap();
		}
	}

	@SuppressWarnings("unchecked")
	private static void build(Map<String, String> translation, Map<String, Object> yamlMap, String prefix)
	{
		yamlMap.forEach((key, value) -> {
			String fullKey = prefix.isEmpty() ? key : (!key.equals(".") ? prefix + "." + key : prefix);
			if (value instanceof String)
			{
				translation.put(fullKey, (String)value);
			}
			else if (value instanceof Map)
			{
				build(translation, (Map<String, Object>)value, fullKey);
			}
			else
			{
				throw new RuntimeException(String.format("Unknown type %s in with key %s", value.getClass(), fullKey));
			}
		});
	}
}
