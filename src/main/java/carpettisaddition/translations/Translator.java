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

import carpettisaddition.utils.Messenger;
import com.google.common.base.Strings;
import net.minecraft.text.BaseText;

public class Translator
{
	private final String translationPath;

	public Translator(String translationPath)
	{
		if (
				Strings.isNullOrEmpty(translationPath) ||
				translationPath.startsWith(".") ||
				translationPath.endsWith(".") ||
				translationPath.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX)
		)
		{
			throw new RuntimeException("Invalid translation path: " + translationPath);
		}
		this.translationPath = translationPath;
	}

	public Translator getDerivedTranslator(String derivedName)
	{
		return new Translator(this.translationPath + "." + derivedName);
	}

	public String getTranslationPath()
	{
		return this.translationPath;
	}

	public BaseText tr(String key, Object... args)
	{
		String translationKey = TranslationConstants.TRANSLATION_KEY_PREFIX + this.translationPath + "." + key;
		return Messenger.tr(translationKey, args);
	}
}
