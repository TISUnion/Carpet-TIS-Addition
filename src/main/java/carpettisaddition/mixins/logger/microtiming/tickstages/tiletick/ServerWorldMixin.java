package carpettisaddition.mixins.logger.microtiming.tickstages.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileTickSubStage;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	private int tileTickOrderCounter = 0;

	@Inject(
			method = "tick",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/server/world/ServerWorld;blockTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
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
							target = "Lnet/minecraft/server/world/ServerWorld;fluidTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/world/ServerTickScheduler;tick()V",
					shift = At.Shift.AFTER
			)
	)
	private void exitStageTileTick(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((ServerWorld)(Object)this, TickStage.UNKNOWN);
	}

	@Inject(
			method = "tick",
			at = {
					@At(
							value = "FIELD",
							target = "Lnet/minecraft/server/world/ServerWorld;blockTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
					),
					@At(
							value = "FIELD",
							target = "Lnet/minecraft/server/world/ServerWorld;fluidTickScheduler:Lnet/minecraft/server/world/ServerTickScheduler;"
					)
			}
	)
	private void resetTileTickOrderCounter(CallbackInfo ci)
	{
		this.tileTickOrderCounter = 0;
	}

	@Inject(method = {"tickBlock", "tickFluid"}, at = @At("HEAD"))
	private void beforeExecuteTileTickEvent(ScheduledTick<?> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, String.valueOf(event.priority.getIndex()));
		MicroTimingLoggerManager.setSubTickStage((ServerWorld)(Object)this, new TileTickSubStage((ServerWorld)(Object)this, event, this.tileTickOrderCounter++));
	}

	@Inject(method = {"tickBlock", "tickFluid"}, at = @At("RETURN"))
	private void afterExecuteTileTickEvent(ScheduledTick<?> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStageDetail((ServerWorld)(Object)this, null);
		MicroTimingLoggerManager.setSubTickStage((ServerWorld)(Object)this, null);
	}
}
