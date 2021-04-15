package carpettisaddition.mixins.logger.microtiming.marker;

import carpettisaddition.logging.loggers.microtiming.marker.MicroTimingMarkerManager;
import carpettisaddition.utils.compact.ServerNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerNetworkHandler.class)
public abstract class ServerNetworkHandlerMixin
{
	@Inject(method = "onHello", at = @At("TAIL"), remap = false)
	private static void onCarpetClientConnected(ServerPlayerEntity playerEntity, PacketByteBuf packetData, CallbackInfo ci)
	{
		MicroTimingMarkerManager.getInstance().sendMarkersForPlayer(playerEntity);
	}
}
