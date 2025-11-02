/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.mixins.logger.raid;

import carpettisaddition.commands.raid.RaidTracker;
import carpettisaddition.logging.loggers.raid.IRaid;
import carpettisaddition.logging.loggers.raid.RaidLogger;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12005
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

@Mixin(Raid.class)
public abstract class RaidMixin implements IRaid
{
	@Shadow private int badOmenLevel;

	@Shadow public abstract boolean isVictory();

	private int previousBadOmenLevel;

	//#if MC >= 12105
	//$$ // `onRaidCreated` is called in {@link RaidManagerMixin#raidLogger_onRaidAddToManager}
	//#else
	@Inject(method = "<init>(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V", at = @At("TAIL"))
	private void raidLogger_onConstruct(CallbackInfo ci)
	{
		RaidLogger.getInstance().onRaidCreated((Raid)(Object)this);
	}
	//#endif

	@Override
	public void onRaidInvalidated$TISCM(RaidLogger.InvalidateReason reason)
	{
		RaidLogger.getInstance().onRaidInvalidated((Raid)(Object)this, reason);
		RaidTracker.getInstance().trackRaidInvalidated(reason);
	}

	@Inject(
			method = "absorbBadOmen",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12005
					//$$ target = "Lnet/minecraft/server/level/ServerPlayer;getEffect(Lnet/minecraft/core/Holder;)Lnet/minecraft/world/effect/MobEffectInstance;"
					//#else
					target = "Lnet/minecraft/world/entity/player/Player;getEffect(Lnet/minecraft/world/effect/MobEffect;)Lnet/minecraft/world/effect/MobEffectInstance;"
					//#endif
			)
	)
	private void raidLogger_onStartBeforeCalculated(
			//#if MC >= 12005
			//$$ CallbackInfoReturnable<Boolean> cir
			//#else
			CallbackInfo ci
			//#endif
	)
	{
		this.previousBadOmenLevel = this.badOmenLevel;
	}

	@Inject(
			method = "absorbBadOmen",
			at = @At(
					value = "INVOKE_ASSIGN",
					shift = At.Shift.AFTER,
					target = "Lnet/minecraft/util/Mth;clamp(III)I"
			)
	)
	private void raidLogger_onStartedAfterCalculated(
			//#if MC >= 12005
			//$$ CallbackInfoReturnable<Boolean> cir
			//#else
			CallbackInfo ci
			//#endif
	)
	{
		if (this.badOmenLevel > 1 && this.badOmenLevel > this.previousBadOmenLevel)
		{
			RaidLogger.getInstance().onBadOmenLevelIncreased((Raid)(Object)this, this.badOmenLevel);
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
							target = "Lnet/minecraft/server/level/ServerLevel;getDifficulty()Lnet/minecraft/world/Difficulty;"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#else
					target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#endif
					ordinal = 0
			)
	)
	private void raidLogger_onInvalidatedByDifficulty(CallbackInfo ci)
	{
		onRaidInvalidated$TISCM(RaidLogger.InvalidateReason.DIFFICULTY_PEACEFUL);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/server/level/ServerLevel;isVillage(Lnet/minecraft/core/BlockPos;)Z"
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#else
					target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#endif
					ordinal = 0
			)
	)
	private void raidLogger_onInvalidatedByPOINotFound(CallbackInfo ci)
	{
		onRaidInvalidated$TISCM(RaidLogger.InvalidateReason.POI_REMOVED_BEFORE_SPAWN);
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
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#else
					target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#endif
					ordinal = 0
			)
	)
	private void raidLogger_onInvalidatedByTimeOut(CallbackInfo ci)
	{
		onRaidInvalidated$TISCM(RaidLogger.InvalidateReason.TIME_OUT);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 12105
							//$$ target = "Lnet/minecraft/world/entity/raid/Raid;playSound(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V"
							//#else
							target = "Lnet/minecraft/world/entity/raid/Raid;playSound(Lnet/minecraft/core/BlockPos;)V"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#else
					target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#endif
					ordinal = 0
			)
	)
	private void raidLogger_onInvalidatedByRaiderCannotSpawn(CallbackInfo ci)
	{
		onRaidInvalidated$TISCM(RaidLogger.InvalidateReason.RAIDER_CANNOT_SPAWN);
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
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#else
					target = "Lnet/minecraft/world/entity/raid/Raid;stop()V",
					//#endif
					ordinal = 0
			)
	)
	private void raidLogger_onInvalidatedByFinished(CallbackInfo ci)
	{
		if (this.isVictory())
		{
			onRaidInvalidated$TISCM(RaidLogger.InvalidateReason.RAID_VICTORY);
		}
		else
		{
			onRaidInvalidated$TISCM(RaidLogger.InvalidateReason.RAID_DEFEAT);
		}
	}

	/*
	 * -----------------------
	 *    onInvalidated end
	 * -----------------------
	 */

	@Inject(
			//#if MC >= 11600
			//$$ method = "setCenter",
			//#else
			method = "setCenter",
			//#endif
			at = @At(value = "HEAD")
	)
	private void raidLogger_onCenterMoved(BlockPos blockPos, CallbackInfo ci)
	{
		RaidLogger.getInstance().onCenterMoved((Raid)(Object)this, blockPos);
	}
}
