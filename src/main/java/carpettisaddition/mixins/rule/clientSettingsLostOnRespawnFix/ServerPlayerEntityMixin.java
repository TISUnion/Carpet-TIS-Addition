package carpettisaddition.mixins.rule.clientSettingsLostOnRespawnFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
	@Shadow public abstract void setClientSettings(ClientSettingsC2SPacket clientSettingsC2SPacket);

	@Nullable
	private ClientSettingsC2SPacket lastClientSettingsC2SPacket = null;

	@Inject(method = "setClientSettings", at = @At("TAIL"))
	private void storeClientSettingsC2SPacket(ClientSettingsC2SPacket clientSettingsC2SPacket, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.clientSettingsLostOnRespawnFix)
		{
			this.lastClientSettingsC2SPacket = clientSettingsC2SPacket;
		}
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void clientSettingsLostOnRespawnFix(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.clientSettingsLostOnRespawnFix)
		{
			ClientSettingsC2SPacket packet = ((ServerPlayerEntityMixin)(Object)oldPlayer).lastClientSettingsC2SPacket;
			if (packet != null)
			{
				this.setClientSettings(packet);
			}
		}
	}
}
