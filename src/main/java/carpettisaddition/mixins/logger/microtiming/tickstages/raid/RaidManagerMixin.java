package carpettisaddition.mixins.logger.microtiming.tickstages.raid;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.entity.raid.RaidManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RaidManager.class)
public abstract class RaidManagerMixin
{
	@Shadow @Final private ServerWorld world;

	@Inject(method = "tick", at = @At("HEAD"))
	private void enterStageRaid(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.RAID);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void exitStageRaid(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.UNKNOWN);
	}
}
