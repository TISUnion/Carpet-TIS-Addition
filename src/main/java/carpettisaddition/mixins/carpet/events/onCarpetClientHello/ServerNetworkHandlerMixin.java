package carpettisaddition.mixins.carpet.events.onCarpetClientHello;

import carpet.network.ServerNetworkHandler;
import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerNetworkHandler.class)
public abstract class ServerNetworkHandlerMixin
{
	@Inject(method = "onHello", at = @At("TAIL"), remap = false)
	private static void onCarpetClientHello(ServerPlayerEntity playerEntity, PacketByteBuf packetData, CallbackInfo ci)
	{
		CarpetTISAdditionServer.INSTANCE.onCarpetClientHello(playerEntity);
	}
}
