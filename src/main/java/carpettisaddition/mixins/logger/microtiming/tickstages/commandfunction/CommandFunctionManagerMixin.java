package carpettisaddition.mixins.logger.microtiming.tickstages.commandfunction;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.function.CommandFunctionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandFunctionManager.class)
public abstract class CommandFunctionManagerMixin
{
	@Inject(method = "tick", at = @At("HEAD"))
	private void enterStageCommandFunctions(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.COMMAND_FUNCTION);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void exitStageCommandFunctions(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(TickStage.UNKNOWN);
	}
}
