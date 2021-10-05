package carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.playeraction;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickstages.PlayerActionTickStageExtra;
import carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.MinecraftServerMixin;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkThreadUtils.class)
public abstract class NetworkThreadUtilsMixin<T>
{
	/**
	 * Stage reset happens in {@link MinecraftServerMixin}
	 */
	@Inject(
			method = "forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V",
			at = @At("HEAD")
	)
	private static <T extends PacketListener> void startProcessPacket(Packet<T> packet, T listener, ThreadExecutor<?> engine, CallbackInfo ci)
	{
		if (engine.isOnThread())
		{
			if (listener instanceof ServerPlayNetworkHandler)
			{
				ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler) listener;
				MicroTimingLoggerManager.setTickStage(TickStage.PLAYER_ACTION);
				MicroTimingLoggerManager.setTickStageExtra(new PlayerActionTickStageExtra(handler.player));
			}
		}
	}
}
