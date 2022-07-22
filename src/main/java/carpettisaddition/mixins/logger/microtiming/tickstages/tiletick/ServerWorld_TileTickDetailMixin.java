package carpettisaddition.mixins.logger.microtiming.tickstages.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileTickSubStage;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.18"))
@Mixin(ServerWorld.class)
public abstract class ServerWorld_TileTickDetailMixin
{
	private int tileTickOrderCounter = 0;

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
