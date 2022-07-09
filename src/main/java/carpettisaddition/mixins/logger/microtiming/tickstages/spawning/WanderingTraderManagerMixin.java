package carpettisaddition.mixins.logger.microtiming.tickstages.spawning;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.WanderingTraderManager;
import org.spongepowered.asm.mixin.Mixin;

//#if MC < 11600
import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(WanderingTraderManager.class)
public abstract class WanderingTraderManagerMixin
{
	//#if MC < 11600
	@Shadow @Final private ServerWorld world;

	@Inject(method = "tick", at = @At("HEAD"))
	private void enterStageWanderingTrader(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.WANDERING_TRADER);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void exitStageWanderingTrader(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.WANDERING_TRADER);
	}
	//#endif
}
