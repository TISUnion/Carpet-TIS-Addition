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

//#if MC >= 11500
import carpet.CarpetSettings;
//#else
//$$ import carpettisaddition.utils.compat.carpet.CarpetSettings;
//#endif

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.mixins.translations.StyleAccessor;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static carpettisaddition.translations.TranslationConstants.*;

public class TISAdditionTranslations
{
	/**
	 * language -> (key -> content)
	 */
	private static final Map<String, Map<String, String>> translationStorage = Maps.newLinkedHashMap();

	public static void loadTranslations()
	{
		TranslationLoader.loadTranslations(translationStorage);
	}

	public static Collection<String> getLanguages()
	{
		return Collections.unmodifiableSet(translationStorage.keySet());
	}

	/**
	 * @param lang the language
	 * @return key -> translated content (translated formatting string)
	 */
	@NotNull
	public static Map<String, String> getTranslations(String lang)
	{
		return translationStorage.getOrDefault(lang, Collections.emptyMap());
	}

	public static String getServerLanguage()
	{
		return CarpetSettings.language.equalsIgnoreCase("none") ? DEFAULT_LANGUAGE : CarpetSettings.language;
	}

	/**
	 * key -> translated formatting string
	 */
	@Nullable
	public static String getTranslationString(String lang, String key)
	{
		return getTranslations(lang.toLowerCase()).get(key);
	}

	public static BaseText translate(BaseText text, String lang)
	{
		return translateText(text, lang);
	}

	public static BaseText translate(BaseText text)
	{
		return translate(text, getServerLanguage());
	}

	public static BaseText translate(BaseText text, ServerPlayerEntity player)
	{
		if (CarpetTISAdditionSettings.ultraSecretSetting.equals("translation"))
		{
			return translate(text);
		}
		return translate(text, ((ServerPlayerEntityWithClientLanguage)player).getClientLanguage$TISCM());
	}

	private static BaseText translateText(BaseText text, @NotNull String lang)
	{
		// quick scan to check if any required translation exists
		boolean[] translationRequired = new boolean[]{false};
		forEachTISCMTranslationText(text, lang, (txt, msgKeyString) -> {
			translationRequired[0] = true;
			return txt;
		});
		if (!translationRequired[0])
		{
			return text;
		}

		// make a copy of the text, and apply translation
		return forEachTISCMTranslationText(Messenger.copy(text), lang, (txt, msgKeyString) -> {

			//#if MC >= 11900
			//$$ TranslatableTextContent content = (TranslatableTextContent) txt.getContent();
			//$$ String txtKey = content.getKey();
			//$$ Object[] txtArgs = content.getArgs();
			//#else
			String txtKey = txt.getKey();
			Object[] txtArgs = txt.getArgs();
			//#endif

			if (msgKeyString == null)
			{
				CarpetTISAdditionServer.LOGGER.warn("TISCM: Unknown translation key {}", txtKey);
				return txt;
			}

			BaseText newText;
			try
			{
				newText = Messenger.format(msgKeyString, txtArgs);
			}
			catch (IllegalArgumentException e)
			{
				newText = Messenger.s(msgKeyString);
			}

			// migrating text data
			newText.getSiblings().addAll(txt.getSiblings());
			newText.setStyle(txt.getStyle());

			return newText;
		});
	}

	private static BaseText forEachTISCMTranslationText(BaseText text, @NotNull String lang, TextModifier modifier)
	{
		if (
				//#if MC >= 11900
				//$$ text.getContent() instanceof TranslatableTextContent
				//#else
				text instanceof TranslatableText
				//#endif
		)
		{
			//#if MC >= 11900
			//$$ TranslatableTextContent translatableText = (TranslatableTextContent)text.getContent();
			//#else
			TranslatableText translatableText = (TranslatableText)text;
			//#endif

			// translate arguments
			Object[] args = translatableText.getArgs();
			for (int i = 0; i < args.length; i++)
			{
				Object arg = args[i];
				if (arg instanceof BaseText)
				{
					BaseText newText = forEachTISCMTranslationText((BaseText)arg, lang, modifier);
					if (newText != arg)
					{
						args[i] = newText;
					}
				}
			}

			// do translation logic
			if (translatableText.getKey().startsWith(TRANSLATION_KEY_PREFIX))
			{
				String msgKeyString = getTranslationString(lang, translatableText.getKey());
				if (msgKeyString == null && !lang.equals(DEFAULT_LANGUAGE))
				{
					msgKeyString = getTranslationString(DEFAULT_LANGUAGE, translatableText.getKey());
				}
				text = modifier.apply(
						//#if MC >= 11900
						//$$ text,
						//#else
						translatableText,
						//#endif
						msgKeyString
				);
			}
		}

		// translate hover text
		HoverEvent hoverEvent = ((StyleAccessor)text.getStyle()).getHoverEventField();
		if (hoverEvent != null)
		{
			//#if MC >= 11600

			Object hoverText = hoverEvent.getValue(hoverEvent.getAction());
			if (hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT && hoverText instanceof BaseText)
			{
				 BaseText newText = forEachTISCMTranslationText((BaseText)hoverText, lang, modifier);
				 if (newText != hoverText)
				 {
					 text.setStyle(text.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, newText)));
				 }
			}
			//#else
			//$$ Text hoverText = hoverEvent.getValue();
			//$$ BaseText newText = forEachTISCMTranslationText((BaseText)hoverText, lang, modifier);
			//$$ if (newText != hoverText)
			//$$ {
			//$$ 	text.getStyle().setHoverEvent(new HoverEvent(hoverEvent.getAction(), newText));
			//$$ }
			//#endif
		}

		// translate sibling texts
		List<Text> siblings = text.getSiblings();
		for (int i = 0; i < siblings.size(); i++)
		{
			Text sibling = siblings.get(i);
			BaseText newText = forEachTISCMTranslationText((BaseText)sibling, lang, modifier);
			if (newText != sibling)
			{
				siblings.set(i, newText);
			}
		}
		return text;
	}

	@FunctionalInterface
	private interface TextModifier
	{
		BaseText apply(
				//#if MC >= 11900
				//$$ MutableText translatableText,
				//#else
				TranslatableText translatableText,
				//#endif
				@Nullable String msgKeyString
		);
	}
}
