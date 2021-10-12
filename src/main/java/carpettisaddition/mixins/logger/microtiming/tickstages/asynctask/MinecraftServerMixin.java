package carpettisaddition.mixins.logger.microtiming.tickstages.asynctask;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.playeraction.NetworkThreadUtilsMixin;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Inject(method = "method_16208", at = @At("HEAD"))
	private void enterStageAsyncTask(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.ASYNC_TASK);
	}

	@Inject(method = "method_16208", at = @At("TAIL"))
	private void exitStageAsyncTask(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.UNKNOWN);
	}

	/**
	 * Reset potential tick stage (extra) set in {@link NetworkThreadUtilsMixin}
	 */
	@Inject(method = "runTask", at = @At("RETURN"))
	void cleanSubStageInStagePlayerAction(CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.ASYNC_TASK);
	}
}
