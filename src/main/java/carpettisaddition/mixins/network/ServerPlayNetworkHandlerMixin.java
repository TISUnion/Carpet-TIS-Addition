package carpettisaddition.mixins.network;

import carpettisaddition.network.TISCMProtocol;
import carpettisaddition.network.TISCMServerPacketHandler;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
	@Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
	private void onCustomPayload$TISCM(CustomPayloadC2SPacket packet, CallbackInfo ci)
	{
		Identifier channel = ((CustomPayloadC2SPacketAccessor)packet).getChannel();
		if (TISCMProtocol.CHANNEL.equals(channel))
		{
			TISCMServerPacketHandler.getInstance().dispatch((ServerPlayNetworkHandler)(Object)this, ((CustomPayloadC2SPacketAccessor)packet).getData());
			ci.cancel();
		}
	}
}
