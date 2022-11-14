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
					//$$ target = "Lnet/minecraft/text/TranslatableText;forEachPart(Ljava/lang/String;Ljava/util/function/Consumer;)V"
					//#else
					target = "Lnet/minecraft/text/TranslatableText;setTranslation(Ljava/lang/String;)V"
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
