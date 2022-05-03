package carpettisaddition.mixins.translations;

import carpet.logging.HUDController;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(HUDController.class)
public abstract class HUDControllerMixin
{
	/**
	 * This handle all TISCM translation in CM hud logger
	 */
	@ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
	private static Text applyTISCarpetTranslationToHudLoggerMessage(Text hudMessage, /* parent method parameters -> */ ServerPlayerEntity player, Text hudMessage_)
	{
		if (player != null)  // fabric carpet has a null check, so let's do the same
		{
			hudMessage = TISAdditionTranslations.translate((MutableText)hudMessage, player);
		}
		return hudMessage;
	}
}
