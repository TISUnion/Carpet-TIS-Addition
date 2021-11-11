package carpettisaddition.mixins.logger.microtiming.api;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Inject(
			method = "tickWorlds",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void beforeTickingWorld(BooleanSupplier shouldKeepTicking, CallbackInfo ci, Iterator<ServerWorld> iter, ServerWorld serverWorld)
	{
		MicroTimingLoggerManager.setCurrentWorld(serverWorld);
	}
	@Inject(
			method = "tickWorlds",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V",
					shift = At.Shift.AFTER
			)
	)
	private void afterTickingWorld(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setCurrentWorld(null);
	}
}
