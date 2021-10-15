package carpettisaddition.mixins.logger.microtiming.tickstages.dragonfight;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonFight.class)
public abstract class EnderDragonFightMixin
{
	@Shadow @Final private ServerWorld world;

	@Inject(method = "tick", at = @At("HEAD"))
	private void enterDragonFightTickStage(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.DRAGON_FIGHT);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void exitDragonFightTickStage(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.UNKNOWN);
	}
}
