package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.tickstages.PlayerActionTickStageExtra;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkThreadUtils.class)
public abstract class NetworkThreadUtilsMixin
{
	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(
			method = "method_11072",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/Packet;apply(Lnet/minecraft/network/listener/PacketListener;)V"
			)
	)
	private static void startProcessPacket(PacketListener packetListener, Packet<?> packet, CallbackInfo ci)
	{
		if (packetListener instanceof ServerPlayNetworkHandler)
		{
			ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler)packetListener;
			MicroTimingLoggerManager.setTickStageExtra(new PlayerActionTickStageExtra(handler.player));
		}
	}

	@SuppressWarnings("UnresolvedMixinReference")
	@Inject(
			method = "method_11072",
			at = @At(
					value = "INVOKE",
					shift = At.Shift.AFTER,
					target = "Lnet/minecraft/network/Packet;apply(Lnet/minecraft/network/listener/PacketListener;)V"
			)
	)
	private static void endProcessPacket(PacketListener packetListener, Packet<?> packet, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageExtra(null);
	}
}
