package carpettisaddition.mixins.translations;

import carpettisaddition.translations.ServerPlayerEntityWithClientLanguage;
import carpettisaddition.translations.TISAdditionTranslations;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements ServerPlayerEntityWithClientLanguage
{
	private String clientLanguage$CTA = "en_US";

	@Inject(method = "setClientSettings", at = @At("HEAD"))
	private void recordClientLanguage(ClientSettingsC2SPacket packet, CallbackInfo ci)
	{
		this.clientLanguage$CTA = packet.language();
	}

	@Override
	public String getClientLanguage$CTA()
	{
		return this.clientLanguage$CTA;
	}

	/**
	 * This handle all TISCM translation on chat messages
	 */
	@ModifyVariable(
			method = "method_43502",
			at = @At("HEAD"),
			argsOnly = true
	)
	private Text applyTISCarpetTranslationToChatMessage(Text message)
	{
		if (message instanceof MutableText)
		{
			message = TISAdditionTranslations.translate((MutableText)message, (ServerPlayerEntity)(Object)this);
		}
		return message;
	}
}
