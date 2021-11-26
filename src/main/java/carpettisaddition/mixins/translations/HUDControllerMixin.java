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
	@ModifyVariable(method = "addMessage", at = @At("HEAD"), argsOnly = true, remap = false)
	private static BaseText applyTISCarpetTranslationToHudLoggerMessage(BaseText hudMessage, /* parent method parameters -> */ PlayerEntity player, BaseText hudMessage_)
	{
		return TISAdditionTranslations.translate(hudMessage, (ServerPlayerEntity)player);
	}
}
