package carpettisaddition.mixins.translations;

import carpet.logging.Logger;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Logger.class)
public abstract class LoggerMixin
{
	@ModifyVariable(method = "sendPlayerMessage", at = @At("HEAD"), argsOnly = true, remap = false)
	private BaseText[] applyTISCarpetTranslationToHudMessage(BaseText[] messages, /* parent method parameters -> */ ServerPlayerEntity player, BaseText... messages_)
	{
		for (int i = 0; i < messages.length; i++)
		{
			messages[i] = TISAdditionTranslations.translate(messages[i], player);
		}
		return messages;
	}
}
