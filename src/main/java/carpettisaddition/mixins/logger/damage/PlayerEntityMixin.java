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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	protected PlayerEntityMixin(EntityType<? extends LivingEntity> type, Level world)
	{
		super(type, world);
	}

	// at the start of player damage calculation
	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/player/Player;removeEntitiesOnShoulder()V"
			)
	)
	private void onDamageStarted(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) DamageSource source, @Local(argsOnly = true) float amount)
	{
		DamageLogger.create(this, source, amount);
	}

	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			slice = @Slice(
					from = @At(
							value = "FIELD",
							target = "Lnet/minecraft/world/entity/player/Abilities;invulnerable:Z"
					)
			),
			at = @At(
					value = "RETURN",
					shift = At.Shift.BEFORE,  // before onDamageEnded in LivingEntityAndPlayerEntityMixins$DamageMixin
					ordinal = 0
			)
	)
	private void onInvulnerableAbilityCancelledDamage(CallbackInfoReturnable<Boolean> cir)
	{
		((DamageLoggerTarget)this).getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(0.0F, ModifyReason.INVULNERABLE));
	}

	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			slice = @Slice(
					from = @At(
							value = "FIELD",
							target = "Lnet/minecraft/world/Difficulty;HARD:Lnet/minecraft/world/Difficulty;"
					)
			),
			at = @At(
					value = "CONSTANT",
					args = "floatValue=0.0F",
					ordinal = 0
			)
	)
	private void onDifficultyModifiedDamage(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) float amount)
	{
		((DamageLoggerTarget)this).getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(amount, ModifyReason.DIFFICULTY));
	}
}
