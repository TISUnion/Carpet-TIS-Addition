package carpettisaddition.mixins.translations;

import carpettisaddition.translations.ServerPlayerEntityWithClientLanguage;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityWithClientLanguage
{
	private String clientLanguage$TISCM = "en_US";

	@Inject(method = "setClientSettings", at = @At("HEAD"))
	private void recordClientLanguage(ClientSettingsC2SPacket packet, CallbackInfo ci)
	{
		this.clientLanguage$TISCM = ((ClientSettingsC2SPacketAccessor)packet).getLanguage$TISCM();
	}

	@Override
	public String getClientLanguage$TISCM()
	{
		return this.clientLanguage$TISCM;
	}

	/**
	 * This handle all TISCM translation on chat messages
	 */
	@ModifyVariable(
			method = {
					//#if MC >= 11600
					//$$ "sendMessage(Lnet/minecraft/text/Text;Z)V",
					//$$ "sendMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V",
					//#else
					"addChatMessage",
					"sendChatMessage",
					//#endif
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
