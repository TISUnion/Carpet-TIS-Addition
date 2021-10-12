package carpettisaddition.mixins.logger.microtiming.tickstages.entity;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.PlayerEntitySubStage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
	@Shadow public ServerPlayerEntity player;

	@Inject(method = "tick", at = @At("HEAD"))
	void startStageDetailTickPlayer(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage(new PlayerEntitySubStage(this.player));
	}

	@Inject(method = "tick", at = @At("RETURN"))
	void endStageDetailTickPlayer(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setSubTickStage(null);
	}
}
