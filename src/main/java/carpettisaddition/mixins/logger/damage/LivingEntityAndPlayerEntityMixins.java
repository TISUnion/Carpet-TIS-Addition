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

package carpettisaddition.mixins.logger.damage;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.damage.interfaces.DamageLoggerTarget;
import carpettisaddition.logging.loggers.damage.modifyreasons.ModifyReason;
import carpettisaddition.logging.loggers.damage.modifyreasons.StatusEffectModifyReason;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

//#if MC >= 12102
//$$ import net.minecraft.server.world.ServerWorld;
//#endif

// some same mixins
public abstract class LivingEntityAndPlayerEntityMixins
{
	@Mixin({LivingEntity.class, PlayerEntity.class})
	public static class ApplyDamageMixin
	{
		@Inject(
				method = "applyDamage",
				at = @At(
						value = "INVOKE_ASSIGN",
						target = "Ljava/lang/Math;max(FF)F"
				)
		)
		private void onAbsorptionReducedDamage(CallbackInfo ci, @Local(argsOnly = true) float amount)
		{
			((DamageLoggerTarget) this).getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
					amount, new StatusEffectModifyReason(StatusEffects.ABSORPTION)
			));
		}

		// at the end of damage calculation
		@Inject(method = "applyDamage", at = @At("RETURN"))
		private void onDamageApplyDone(
				CallbackInfo ci, @Local(argsOnly = true) DamageSource source, @Local(argsOnly = true) float amount
				//#if MC >= 12102
				//$$ , @Local(argsOnly = true) ServerWorld serverWorld
				//#endif
		)
		{
			if (DamageLogger.isLoggerActivated())
			{
				LivingEntity entity = (LivingEntity) (Object) this;
				Optional<DamageLogger.Tracker> logger = ((DamageLoggerTarget) this).getDamageTracker();
				if (entity.isInvulnerableTo(
						//#if MC >= 12102
						//$$ serverWorld,
						//#endif
						source
				))
				{
					amount = 0.0F;
					logger.ifPresent(tracker -> tracker.modifyDamage(0.0F, ModifyReason.IMMUNE));
				}
				float finalAmount = amount;
				logger.ifPresent(tracker -> tracker.flush(finalAmount, entity.getHealth()));
			}
		}
	}

	@Mixin({LivingEntity.class, PlayerEntity.class, ServerPlayerEntity.class})
	public static class DamageMixin
	{
		@Inject(
				//#disable-remap
				method = "damage",
				//#enable-remap
				at = @At(value = "RETURN")
		)
		private void onDamageEnded(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) float amount)
		{
			if (DamageLogger.isLoggerActivated())
			{
				LivingEntity entity = (LivingEntity) (Object) this;
				if (!cir.getReturnValue())
				{
					// return false means actually received no damage for some reason,
					// logger.flush after regular damage calculation might not be called so here's a backup call
					((DamageLoggerTarget) this).getDamageTracker().ifPresent(tracker -> tracker.flush(0.0F, entity.getHealth()));
				}
				((DamageLoggerTarget) this).setDamageTracker(null);
			}
		}
	}
}
