package carpettisaddition.mixins.logger.microtiming.tickstages.commandfunction;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
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
}
