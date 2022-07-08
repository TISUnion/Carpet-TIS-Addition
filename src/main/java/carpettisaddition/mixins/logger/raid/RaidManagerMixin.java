package carpettisaddition.mixins.logger.raid;

import carpettisaddition.logging.loggers.raid.IRaid;
import carpettisaddition.logging.loggers.raid.RaidLogger;
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
							//#if MC >= 11600
							//$$ target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$Key;)Z"
							//#else
							target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
					//#else
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					//#endif
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onInvalidatedByGamerule(CallbackInfo ci, Iterator<Raid> iterator, Raid raid)
	{
		((IRaid)raid).onRaidInvalidated(RaidLogger.InvalidateReason.GAMERULE_DISABLE);
	}
}
