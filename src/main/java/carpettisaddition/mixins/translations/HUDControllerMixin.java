package carpettisaddition.mixins.translations;

import carpet.logging.HUDController;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HUDController.class)
public abstract class HUDControllerMixin
{
	@ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
	private static BaseText applyTISCarpetTranslationToHudMessage(BaseText hudMessage, /* parent method parameters -> */ ServerPlayerEntity player, BaseText hudMessage_)
	{
		if (player != null)  // fabric carpet has a null check, so let's do the same
		{
			hudMessage = TISAdditionTranslations.translate(hudMessage, player);
		}
		return hudMessage;
	}
}
