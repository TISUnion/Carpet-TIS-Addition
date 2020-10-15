package carpettisaddition.mixins.logger.raid;

import carpettisaddition.interfaces.IRaid_RaidLogger;
import carpettisaddition.logging.loggers.RaidLogger;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaidManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;


@Mixin(RaidManager.class)
public abstract class RaidManagerMixin
{
	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onInvalidatedByGamerule(CallbackInfo ci, Iterator<Raid> iterator, Raid raid)
	{
		((IRaid_RaidLogger)raid).onRaidInvalidated(RaidLogger.InvalidateReason.GAMERULE_DISABLE);
	}
}
