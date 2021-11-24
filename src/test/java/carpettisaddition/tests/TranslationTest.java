package carpettisaddition.tests;

import carpettisaddition.translations.TISAdditionTranslations;
import com.google.common.collect.Maps;
import junit.framework.TestCase;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TranslationTest extends TestCase
{
	private static final String CM_TRANSLATION_PREFIX = "carpettisaddition.carpet_extension";

	public void testTranslationConsistency()
	{
		TISAdditionTranslations.loadTranslations();
		Map<String, List<String>> translationKeys = Maps.newLinkedHashMap();
		TISAdditionTranslations.translationStorage.forEach((lang, translations) -> translationKeys.put(
				lang, translations.keySet().stream().
						filter(key -> !key.startsWith(CM_TRANSLATION_PREFIX)).
						collect(Collectors.toList())
		));
		if (translationKeys.size() >= 1)
		{
			String stdLang = translationKeys.keySet().iterator().next();
			List<String> stdKeys = translationKeys.get(stdLang);
			int stdSize = stdKeys.size();

			// translation size
			for (Map.Entry<String, List<String>> entry : translationKeys.entrySet())
			{
				String testLang = entry.getKey();
				int testSize = entry.getValue().size();
				assertEquals(String.format("STD language %s has size %d, but language %s has size %d", stdLang, stdSize, testLang, testSize), stdSize, testSize);
			}

			// translation key order
			for (int i = 0; i < stdKeys.size(); i++)
			{
				String stdKey = stdKeys.get(i);
				for (Map.Entry<String, List<String>> entry : translationKeys.entrySet())
				{
					String testLang = entry.getKey();
					String testKey = entry.getValue().get(i);
					assertEquals(String.format("Key #%d, excepted %s in %s, found %s in %s\n", i, stdKey, stdLang, testKey, testLang), stdKey, testKey);
				}
			}
		}
	}
}
