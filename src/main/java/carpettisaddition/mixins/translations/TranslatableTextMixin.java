package carpettisaddition.mixins.translations;

import carpettisaddition.translations.TISAdditionTranslations;
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

	@ModifyArg(
			method = "updateTranslations",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/TranslatableText;setTranslation(Ljava/lang/String;)V"
			)
	)
	private String applyTISCarpetTranslation(String vanillaTranslatedFormattingString)
	{
		if (vanillaTranslatedFormattingString.equals(this.key))  // translation failed
		{
			String tiscmTranslated = TISAdditionTranslations.translateKey(TISAdditionTranslations.DEFAULT_LANGUAGE, this.key);
			if (tiscmTranslated != null)
			{
				return tiscmTranslated;
			}
		}
		return vanillaTranslatedFormattingString;
	}
}
