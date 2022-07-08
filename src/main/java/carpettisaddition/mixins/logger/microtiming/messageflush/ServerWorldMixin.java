package carpettisaddition.mixins.logger.microtiming.messageflush;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Inject(
			method = "tick",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;tickTime()V")
	)
	private void flushMessageOnTimeUpdate(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.microTimingTickDivision == TickDivision.WORLD_TIMER)
		{
			if (DimensionWrapper.of((ServerWorld)(Object)this).equals(DimensionWrapper.OVERWORLD))  // only flush messages at overworld time update
			{
				MicroTimingLoggerManager.flushMessages();
			}
		}
	}
}
