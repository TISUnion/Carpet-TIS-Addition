package carpettisaddition.mixins.logger.raid;

import carpettisaddition.helpers.RaidTracker;
import carpettisaddition.interfaces.IRaid;
import carpettisaddition.logging.logHelpers.RaidLogHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.village.raid.Raid;
import net.minecraft.util.math.BlockPos;
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

	@Shadow public abstract boolean hasWon();

	private int previousBadOmenLevel;

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
					target = "Lnet/minecraft/entity/player/PlayerEntity;getStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/entity/effect/StatusEffectInstance;"
			)
	)
	private void onStartBeforeCalculated(PlayerEntity player, CallbackInfo ci)
	{
		this.previousBadOmenLevel = this.badOmenLevel;
	}

	@Inject(
			method = "start",
			at = @At(
					value = "INVOKE_ASSIGN",
					shift = At.Shift.AFTER,
					target = "Lnet/minecraft/util/math/MathHelper;clamp(III)I"
			)
	)
	private void onStarted(PlayerEntity player, CallbackInfo ci)
	{
		if (this.badOmenLevel > 1 && this.badOmenLevel > this.previousBadOmenLevel)
		{
			RaidLogHelper.onBadOmenLevelIncreased((Raid)(Object)this, this.badOmenLevel);
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
							target = "Lnet/minecraft/world/WorldAccess;getDifficulty()Lnet/minecraft/world/Difficulty;"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
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
					target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
					ordinal = 0
			)
	)
	private void onInvalidatedByPOINotFound(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogHelper.InvalidateReason.POI_REMOVED_BEFORE_SPAWN);
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
					target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
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
							target = "Lnet/minecraft/village/raid/Raid;playRaidHorn(Lnet/minecraft/util/math/BlockPos;)V"
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
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
					target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
					ordinal = 0
			)
	)
	private void onInvalidatedByFinished(CallbackInfo ci)
	{
		if (this.hasWon())
		{
			onRaidInvalidated(RaidLogHelper.InvalidateReason.RAID_VICTORY);
		}
		else
		{
			onRaidInvalidated(RaidLogHelper.InvalidateReason.RAID_DEFEAT);
		}
	}

	/*
	 * -----------------------
	 *    onInvalidated end
	 * -----------------------
	 */

	@Inject(
			method = "setCenter",
			at = @At(value = "HEAD")
	)
	void onCenterMoved(BlockPos blockPos, CallbackInfo ci)
	{
		RaidLogHelper.onCenterMoved((Raid)(Object)this, blockPos);
	}
}
