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

package carpettisaddition.mixins.translations;

import carpettisaddition.translations.TISAdditionTranslations;
import carpettisaddition.translations.TranslationConstants;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TranslatableText.class)
public abstract class TranslatableTextMixin
{
	@Shadow @Final private String key;

	/**
	 * This handles all TISCM translation when a TranslatableText is directly accessed as a String etc.
	 */
	@ModifyArg(
			method = "updateTranslations",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11900
					//$$ target = "Lnet/minecraft/text/TranslatableTextContent;forEachPart(Ljava/lang/String;Ljava/util/function/Consumer;)V"
					//#elseif MC >= 11800
					target = "Lnet/minecraft/text/TranslatableText;forEachPart(Ljava/lang/String;Ljava/util/function/Consumer;)V"
					//#else
					//$$ target = "Lnet/minecraft/text/TranslatableText;setTranslation(Ljava/lang/String;)V"
					//#endif
			)
	)
	private String applyTISCarpetTranslation(String vanillaTranslatedFormattingString)
	{
		if (this.key.startsWith(TranslationConstants.TRANSLATION_KEY_PREFIX) && vanillaTranslatedFormattingString.equals(this.key))
		{
			String tiscmTranslated = TISAdditionTranslations.getTranslationString(TISAdditionTranslations.getServerLanguage(), this.key);
			if (tiscmTranslated != null)
			{
				return tiscmTranslated;
			}
		}
		return vanillaTranslatedFormattingString;
	}
}
