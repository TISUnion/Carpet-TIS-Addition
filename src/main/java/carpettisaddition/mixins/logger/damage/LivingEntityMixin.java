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
import carpettisaddition.logging.loggers.damage.modifyreasons.ArmorModifyReason;
import carpettisaddition.logging.loggers.damage.modifyreasons.EnchantmentModifyReason;
import carpettisaddition.logging.loggers.damage.modifyreasons.ModifyReason;
import carpettisaddition.logging.loggers.damage.modifyreasons.StatusEffectModifyReason;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DamageLoggerTarget
{
	@Shadow protected float
			//#if MC >= 11500
			lastDamageTaken;
			//#else
			//$$ field_6253;
			//#endif

	@Shadow public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Shadow public abstract Iterable<ItemStack> getArmorItems();

	@Nullable
	private DamageLogger.Tracker damageTracker$TISCM = null;

	@Override
	public Optional<DamageLogger.Tracker> getDamageTracker()
	{
		return Optional.ofNullable(this.damageTracker$TISCM);
	}

	@Override
	public void setDamageTracker(@Nullable DamageLogger.Tracker tracker)
	{
		this.damageTracker$TISCM = tracker;
	}

	// at the start of general damage calculation
	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z"
			)
	)
	private void onDamageStarted(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		DamageLogger.create((LivingEntity)(Object)this, source, amount);
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "CONSTANT",
					args = "floatValue=0.75F"
			)
	)
	private void onHelmetReducedAnvilDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(amount * 0.75F, ModifyReason.HELMET));
	}

	@Inject(
			method = "damage",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"
					)
			),
			at = @At(
					//#if MC >= 11904
					//$$ value = "FIELD",
					//$$ target = "Lnet/minecraft/class_8103;IS_PROJECTILE:Lnet/minecraft/registry/tag/TagKey;",
					//#else
					value = "INVOKE",
					target = "Lnet/minecraft/entity/damage/DamageSource;isProjectile()Z",
					//#endif
					ordinal = 0
			)
	)
	private void onShieldReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(amount, ModifyReason.SHIELD));
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "FIELD",
					//#if MC >= 11500
					target = "Lnet/minecraft/entity/LivingEntity;lastDamageTaken:F",
					//#else
					//$$ target = "Lnet/minecraft/entity/LivingEntity;field_6253:F",
					//#endif
					ordinal = 0
			)
	)
	private void onRecentHintReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		final float last =
				//#if MC >= 11500
				this.lastDamageTaken;
				//#else
				//$$ this.field_6253;
				//#endif

		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				Math.max(amount - last, 0.0F), ModifyReason.RECENTLY_HIT
		));
	}

	@Inject(method = "applyArmorToDamage", at = @At("RETURN"))
	private void onArmorReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				amount, new ArmorModifyReason((LivingEntity)(Object)this)
		));
	}

	@Inject(
			method = "applyEnchantmentsToDamage",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Ljava/lang/Math;max(FF)F"
			)
	)
	private void onResistanceReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				amount, new StatusEffectModifyReason(StatusEffects.RESISTANCE, this.getStatusEffect(StatusEffects.RESISTANCE).getAmplifier())
		));
	}

	@Inject(
			method = "applyEnchantmentsToDamage",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/entity/DamageUtil;getInflictedDamage(FF)F"
			)
	)
	private void onEnchantmentReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> {
			int epf = EnchantmentHelper.getProtectionAmount(this.getArmorItems(), source);  // vanilla copy
			tracker.modifyDamage(amount, new EnchantmentModifyReason(epf));
		});
	}
}
