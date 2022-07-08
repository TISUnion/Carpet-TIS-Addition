package carpettisaddition.mixins.translations;

import carpet.logging.HUDController;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
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
	private static BaseText applyTISCarpetTranslationToHudLoggerMessage(
			BaseText hudMessage,
			/* parent method parameters vvv */

			//#if MC >= 11600
			//$$ ServerPlayerEntity player,
			//#else
			PlayerEntity player,
			//#endif
			BaseText hudMessage_
	)
	{
		if (player != null)
		{
			hudMessage = TISAdditionTranslations.translate(hudMessage, (ServerPlayerEntity)player);
		}
		return hudMessage;
	}
}
