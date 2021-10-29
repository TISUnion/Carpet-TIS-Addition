package carpettisaddition.mixins.logger.microtiming.tickstages.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/world/ServerWorld;blockTickScheduler:Lnet/minecraft/class_6757;"
			)
	)
	private void enterStageTileTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.TILE_TICK);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							target = "Lnet/minecraft/server/world/ServerWorld;fluidTickScheduler:Lnet/minecraft/class_6757;"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/class_6757;method_39377(JILjava/util/function/BiConsumer;)V",
					shift = At.Shift.AFTER
			)
	)
	private void exitStageTileTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.UNKNOWN);
	}
}
