package carpettisaddition.mixins.logger.microtiming.messageflush;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickDivision;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Inject(
			//#if MC >= 11700
			//$$ method = "runTasksTillTickEnd",
			//#else
			method = "method_16208",
			//#endif
			at = @At("HEAD")
	)
	private void flushMessageOnPlayerAction(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.microTimingTickDivision == TickDivision.PLAYER_ACTION)
		{
			MicroTimingLoggerManager.flushMessages();
		}
	}
}
