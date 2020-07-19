package carpettisaddition.mixins.logger;

import carpettisaddition.interfaces.IRaid;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.logHelpers.RaidLogHelper;
import net.minecraft.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Raid.class)
public abstract class Raid_raidLoggerMixin implements IRaid
{
	private RaidLogHelper logHelper;

	@Override
	public RaidLogHelper getRaidLogHelper()
	{
		return this.logHelper;
	}

	@Inject(
			method = "<init>(ILnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;)V",
			at = @At(value = "RETURN")
	)
	private void onConstruct(CallbackInfo ci)
	{
		if (ExtensionLoggerRegistry.__raid)
		{
			logHelper = new RaidLogHelper((Raid) (Object) this);
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
		if (logHelper != null)
		{
			logHelper.onRaidInvalidated(RaidLogHelper.InvalidateReason.DIFFICULTY_PEACEFUL);
		}
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
		if (logHelper != null)
		{
			logHelper.onRaidInvalidated(RaidLogHelper.InvalidateReason.POI_NOT_FOUND);
		}
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "intValue=48000",
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
		if (logHelper != null)
		{
			logHelper.onRaidInvalidated(RaidLogHelper.InvalidateReason.TIME_OUT);
		}
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
		if (logHelper != null)
		{
			logHelper.onRaidInvalidated(RaidLogHelper.InvalidateReason.RAIDER_CANNOT_SPAWN);
		}
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
		if (logHelper != null)
		{
			logHelper.onRaidInvalidated(RaidLogHelper.InvalidateReason.FINISHED);
		}
	}

	/*
	 * -----------------------
	 *    onInvalidated end
	 * -----------------------
	 */


}
