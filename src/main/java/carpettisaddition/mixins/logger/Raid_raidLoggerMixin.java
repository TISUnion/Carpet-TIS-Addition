package carpettisaddition.mixins.logger;

import carpettisaddition.helpers.RaidTracker;
import carpettisaddition.interfaces.IRaid;
import carpettisaddition.logging.logHelpers.RaidLogHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Raid.class)
public abstract class Raid_raidLoggerMixin implements IRaid
{
	@Shadow private int badOmenLevel;

	@Inject(
			method = "<init>(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		RaidLogHelper.onRaidCreated((Raid)(Object)this);
	}

	@Override
	public void onRaidInvalidated(RaidLogHelper.InvalidateReason reason)
	{
		RaidLogHelper.onRaidInvalidated((Raid)(Object)this, reason);
		RaidTracker.trackRaidInvalidated(reason);
	}

	@Inject(
			method = "start",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/Raid;getMaxAcceptableBadOmenLevel()I",
					shift = At.Shift.AFTER
			)
	)
	private void onStarted(PlayerEntity player, CallbackInfo ci)
	{
		if (this.badOmenLevel > 1)
		{
			RaidLogHelper.onBadOmenLevelIncreased((Raid)(Object)this);
		}
	}

	/*
	 * -------------------------
	 *    onInvalidated start
	 * -------------------------
	 */

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/IWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					ordinal = 0
			)
	)
	private void onInvalidatedByDifficulty(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogHelper.InvalidateReason.DIFFICULTY_PEACEFUL);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/world/ServerWorld;isNearOccupiedPointOfInterest(Lnet/minecraft/util/math/BlockPos;)Z"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					ordinal = 0
			)
	)
	private void onInvalidatedByPOINotFound(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogHelper.InvalidateReason.POI_CLEARED);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "longValue=48000",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					ordinal = 0
			)
	)
	private void onInvalidatedByTimeOut(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogHelper.InvalidateReason.TIME_OUT);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/raid/Raid;playRaidHorn(Lnet/minecraft/util/math/BlockPos;)V"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					ordinal = 0
			)
	)
	private void onInvalidatedByRaiderCannotSpawn(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogHelper.InvalidateReason.RAIDER_CANNOT_SPAWN);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=600"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					ordinal = 0
			)
	)
	private void onInvalidatedByFinished(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogHelper.InvalidateReason.RAID_FINISHED);
	}

	/*
	 * -----------------------
	 *    onInvalidated end
	 * -----------------------
	 */


}
