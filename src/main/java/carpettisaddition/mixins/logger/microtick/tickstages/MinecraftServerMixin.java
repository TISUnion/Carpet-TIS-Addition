package carpettisaddition.mixins.logger.microtick.tickstages;

import carpettisaddition.logging.loggers.microtick.MicroTickLoggerManager;
import carpettisaddition.logging.loggers.microtick.tickstages.StringTickStage;
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
		MicroTickLoggerManager.setTickStage("AutoSave");
	}

	@Inject(
			method = "method_16208",
			at = @At("HEAD")
	)
	private void onStagePlayerAction(CallbackInfo ci)
	{
		MicroTickLoggerManager.setTickStage("PlayerAction");
		MicroTickLoggerManager.setTickStageExtra(new StringTickStage("SyncTasks including player actions"));
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
		MicroTickLoggerManager.setTickStage("CommandFunction");
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
		MicroTickLoggerManager.setTickStage("Network");
	}
}
