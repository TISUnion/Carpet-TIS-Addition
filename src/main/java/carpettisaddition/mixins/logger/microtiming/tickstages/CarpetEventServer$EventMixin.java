package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import org.spongepowered.asm.mixin.Dynamic;
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
public abstract class CarpetEventServer$EventMixin
{
	/**
	 * {@code @Dynamic} for yeeting UnresolvedMixinReference warning
	 * require = 0 for preventing it from crashing if carpet changes the order of anonymous classes
	 * scarpet stage it's not that important so failure is tolerable
	 */
	@Dynamic
	@Inject(method = "onTick", at = @At("HEAD"), remap = false, require = 0)
	private void onCarpetModStage(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.SCARPET);
	}
}
