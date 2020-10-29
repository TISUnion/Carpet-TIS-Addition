package carpettisaddition.mixins.logger.microtiming.tickstages;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickstages.StringTickStageExtra;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


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

	@Inject(
			method = "method_16208",
			at = @At("HEAD")
	)
	private void onStagePlayerAction(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.PLAYER_ACTION);
		MicroTimingLoggerManager.setTickStageExtra(StringTickStageExtra.SYNC_TASKS);
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
		MicroTimingLoggerManager.setTickStageExtra(StringTickStageExtra.ENTITY_PLAYER);
	}
}
