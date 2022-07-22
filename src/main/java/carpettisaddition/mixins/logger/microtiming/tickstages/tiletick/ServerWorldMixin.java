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
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;blockTickScheduler:Lnet/minecraft/world/tick/WorldTickScheduler;"
					//#else
					target = "Lnet/minecraft/server/world/ServerWorld;blockTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
					//#endif
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
							//#if MC >= 11800
							//$$ target = "Lnet/minecraft/server/world/ServerWorld;fluidTickScheduler:Lnet/minecraft/world/tick/WorldTickScheduler;"
							//#else
							target = "Lnet/minecraft/server/world/ServerWorld;fluidTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/world/tick/WorldTickScheduler;tick(JILjava/util/function/BiConsumer;)V",
					//#else
					target = "Lnet/minecraft/server/world/ServerTickScheduler;tick()V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void exitStageTileTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.UNKNOWN);
	}
}
