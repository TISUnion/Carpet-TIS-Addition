package carpettisaddition.mixins.translations;

import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
	/**
	 * This handle all TISCM translation on chat messages
	 */
	@ModifyVariable(
			method = {
					"addChatMessage",
					"sendChatMessage",
			},
			at = @At("HEAD"),
			argsOnly = true
	)
	private Text applyTISCarpetTranslationToChatMessage(Text message)
	{
		if (message instanceof BaseText)
		{
			message = TISAdditionTranslations.translate((BaseText)message, (ServerPlayerEntity)(Object)this);
		}
		return message;
	}
}
