package carpettisaddition.mixins.logger.lightqueue.compact.tic_tacs;

import carpettisaddition.logging.loggers.lightqueue.IServerLightingProvider;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Tic-TACS mod implements its own lighting executor
 * So in order to make the lightQueue logger be able to successfully record lighting tasks being executed,
 * here comes the Mixin
 */
@Pseudo
@Mixin(
		targets = {
				"net.gegy1000.tictacs.async.worker.LightingExecutor",  // tic-tacs-0.1.2
				"net.gegy1000.tictacs.lighting.LightingExecutor"  // at least it's here when i code this, on 2021/02/25
		},
		remap = false
)
public abstract class LightingExecutorMixin
{
	@Shadow(remap = false)
	private LightingProvider lightingProvider;

	@Dynamic
	@Inject(
			method = "processQueue",
			at = @At(
					value = "INVOKE",
					target = "Ljava/lang/Runnable;run()V",
					shift = At.Shift.AFTER,
					remap = false
			),
			remap = false
	)
	private void onLightingTaskExecuted(CallbackInfo ci)
	{
		if (this.lightingProvider instanceof IServerLightingProvider)
		{
			((IServerLightingProvider)this.lightingProvider).onExecutedLightUpdates();
		}
	}
}
