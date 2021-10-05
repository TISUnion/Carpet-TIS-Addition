package carpettisaddition.mixins.logger.microtiming.tickstages.scarpet;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
		targets = {
				"carpet.script.CarpetEventServer$Event$1",  // TICK
				"carpet.script.CarpetEventServer$Event$2",  // NETHER_TICK
				"carpet.script.CarpetEventServer$Event$3",  // ENDER_TICK
		},
		remap = false
)
public abstract class CarpetEventServer_EventMixin
{
	/**
	 * require = 0 for preventing it from crashing if carpet changes the order of anonymous classes
	 * scarpet stage it's not that important so failure is tolerable
	 */
	@Inject(method = "onTick", at = @At("HEAD"), remap = false, require = 0)
	private void onCarpetModStage(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.SCARPET);
	}
}
