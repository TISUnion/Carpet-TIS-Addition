package carpettisaddition.mixins.logger;

import carpettisaddition.interfaces.IRaid;
import carpettisaddition.logging.logHelpers.RaidLogHelper;
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
public abstract class RaidManager_raidLoggerMixin
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
		if (((IRaid)raid).getRaidLogHelper() != null)
		{
			((IRaid)raid).getRaidLogHelper().onRaidInvalidated(RaidLogHelper.InvalidateReason.GAMERULE_DISABLE);
		}
	}

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Iterator;remove()V",
					ordinal = 0
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onRaidRemoved(CallbackInfo ci, Iterator<Raid> iterator, Raid raid)
	{
		if (((IRaid)raid).getRaidLogHelper() != null)
		{
			((IRaid)raid).getRaidLogHelper().onRaidRemoved();
		}
	}
}
