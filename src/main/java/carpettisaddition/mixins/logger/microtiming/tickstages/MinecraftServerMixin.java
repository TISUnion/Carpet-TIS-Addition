package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Inject(
			method = "tick",
			at = @At(
					value = "CONSTANT",
					args = "stringValue=save"
			)
	)
	private void onStageAutosave(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.AUTO_SAVE);
	}

	@Inject(method = "runTasksTillTickEnd", at = @At("HEAD"))
	private void onStageAsyncTask(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.ASYNC_TASK);
	}

	/**
	 * Reset potential tick stage (extra) set in {@link carpettisaddition.mixins.logger.microtiming.tickstages.NetworkThreadUtilsMixin}
	 */
	@Inject(method = "runTask", at = @At("RETURN"))
	void cleanStageExtraInStagePlayerAction(CallbackInfoReturnable<Boolean> cir)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.ASYNC_TASK);
		MicroTimingLoggerManager.setTickStageExtra(null);
	}

	@Inject(
			method = "tickWorlds",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/function/CommandFunctionManager;tick()V"
			)
	)
	private void onStageCommandFunctions(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.COMMAND_FUNCTION);
	}

	@Inject(
			method = "tickWorlds",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/ServerNetworkIo;tick()V"
			)
	)
	private void onStageNetwork(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.NETWORK);
	}
}
