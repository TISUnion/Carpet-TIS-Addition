package carpettisaddition.mixins.logger.microtiming.tickstages.network;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.ServerNetworkIo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerNetworkIo.class)
public abstract class ServerNetworkIoMixin
{
	@Inject(method = "tick", at = @At("HEAD"))
	private void enterStageNetwork(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.NETWORK);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void exitStageNetwork(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.UNKNOWN);
	}
}
