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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.Raid;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Raid.class)
public abstract class RaidMixin implements IRaid
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
		RaidLogger.getInstance().onRaidCreated((Raid)(Object)this);
	}

	@Override
	public void onRaidInvalidated(RaidLogger.InvalidateReason reason)
	{
		RaidLogger.getInstance().onRaidInvalidated((Raid)(Object)this, reason);
		RaidTracker.getInstance().trackRaidInvalidated(reason);
	}

	@Inject(
			method = "start",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12005
					//$$ target = "Lnet/minecraft/entity/player/PlayerEntity;getStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/entity/effect/StatusEffectInstance;"
					//#else
					target = "Lnet/minecraft/entity/player/PlayerEntity;getStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Lnet/minecraft/entity/effect/StatusEffectInstance;"
					//#endif
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
							target = "Lnet/minecraft/server/world/ServerWorld;getDifficulty()Lnet/minecraft/world/Difficulty;"
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
			)
	)
	private void onInvalidatedByDifficulty(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogger.InvalidateReason.DIFFICULTY_PEACEFUL);
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
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
					//#else
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					//#endif
					ordinal = 0
			)
	)
	private void onInvalidatedByPOINotFound(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogger.InvalidateReason.POI_REMOVED_BEFORE_SPAWN);
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
					//$$ target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
					//#else
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					//#endif
					ordinal = 0
			)
	)
	private void onInvalidatedByTimeOut(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogger.InvalidateReason.TIME_OUT);
	}

	@Inject(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11600
							//$$ target = "Lnet/minecraft/village/raid/Raid;playRaidHorn(Lnet/minecraft/util/math/BlockPos;)V"
							//#else
							target = "Lnet/minecraft/entity/raid/Raid;playRaidHorn(Lnet/minecraft/util/math/BlockPos;)V"
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
			)
	)
	private void onInvalidatedByRaiderCannotSpawn(CallbackInfo ci)
	{
		onRaidInvalidated(RaidLogger.InvalidateReason.RAIDER_CANNOT_SPAWN);
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
					//$$ target = "Lnet/minecraft/village/raid/Raid;invalidate()V",
					//#else
					target = "Lnet/minecraft/entity/raid/Raid;invalidate()V",
					//#endif
					ordinal = 0
			)
	)
	private void onInvalidatedByFinished(CallbackInfo ci)
	{
		if (this.hasWon())
		{
			onRaidInvalidated(RaidLogger.InvalidateReason.RAID_VICTORY);
		}
		else
		{
			onRaidInvalidated(RaidLogger.InvalidateReason.RAID_DEFEAT);
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
			method = "method_20509",
			//#endif
			at = @At(value = "HEAD")
	)
	private void onCenterMoved(BlockPos blockPos, CallbackInfo ci)
	{
		RaidLogger.getInstance().onCenterMoved((Raid)(Object)this, blockPos);
	}
}
