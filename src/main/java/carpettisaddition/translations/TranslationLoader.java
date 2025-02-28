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

package carpettisaddition.translations;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.utils.FileUtils;
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
			String fileContent = FileUtils.readResourceFileAsString(RESOURCE_DIR + "/meta/languages.json");
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
			String fileContent = FileUtils.readResourceFileAsString(String.format("%s/%s.json", RESOURCE_DIR, lang));
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
