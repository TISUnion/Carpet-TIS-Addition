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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

//#if MC >= 12105
//$$ import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
//#endif

//#if MC >= 12100
//$$ import carpettisaddition.utils.EntityUtils;
//$$ import net.minecraft.server.level.ServerLevel;
//$$ import net.minecraft.world.level.Level;
//#endif

//#if MC >= 12005
//$$ import net.minecraft.core.Holder;
//#endif

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements DamageLoggerTarget
{
	@Shadow protected float lastHurt;

	@Shadow public abstract MobEffectInstance getEffect(
			//#if MC >= 12005
			//$$ Holder<MobEffect> effect
			//#else
			MobEffect effect
			//#endif
	);

	//#if MC < 12100
	@Shadow public abstract Iterable<ItemStack> getArmorSlots();
	//#endif

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
			//#disable-remap
			method = "hurt",
			//#enable-remap
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/LivingEntity;isSleeping()Z"
			)
	)
	private void onDamageStarted(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) DamageSource source, @Local(argsOnly = true) float amount)
	{
		DamageLogger.create((LivingEntity)(Object)this, source, amount);
	}

	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			at = @At(
					value = "CONSTANT",
					args = "floatValue=0.75F"
			)
	)
	private void onHelmetReducedAnvilDamage(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) float amount)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(amount * 0.75F, ModifyReason.HELMET));
	}

	//#if MC >= 12105
	//$$ @ModifyExpressionValue(
	//$$ 		method = "hurt",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/entity/LivingEntity;applyItemBlocking(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)F"
	//$$ 		)
	//$$ )
	//$$ private float onShieldReducedDamage(float damageBlockedAmount, @Local(argsOnly = true) float amount)
	//$$ {
	//$$ 	if (damageBlockedAmount > 0)
	//$$ 	{
	//$$ 		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(amount - damageBlockedAmount, ModifyReason.SHIELD));
	//$$ 	}
	//$$ 	return damageBlockedAmount;
	//$$ }
	//#else
	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/world/entity/LivingEntity;hurtCurrentlyUsedShield(F)V"
					)
			),
			at = @At(
					//#if MC >= 11904
					//$$ value = "FIELD",
					//$$ target = "Lnet/minecraft/tags/DamageTypeTags;IS_PROJECTILE:Lnet/minecraft/tags/TagKey;",
					//#else
					value = "INVOKE",
					target = "Lnet/minecraft/world/damagesource/DamageSource;isProjectile()Z",
					//#endif
					ordinal = 0
			)
	)
	private void onShieldReducedDamage(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) float amount)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(amount, ModifyReason.SHIELD));
	}
	//#endif

	@Inject(
			//#disable-remap
			method = "hurt",
			//#enable-remap
			at = @At(
					value = "FIELD",
					//#if MC >= 11500
					target = "Lnet/minecraft/world/entity/LivingEntity;lastHurt:F",
					//#else
					//$$ target = "Lnet/minecraft/world/entity/LivingEntity;lastHurt:F",
					//#endif
					ordinal = 0
			)
	)
	private void onRecentHintReducedDamage(CallbackInfoReturnable<Boolean> cir, @Local(argsOnly = true) float amount)
	{
		final float last = this.lastHurt;

		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				Math.max(amount - last, 0.0F), ModifyReason.RECENTLY_HIT
		));
	}

	@Inject(method = "getDamageAfterArmorAbsorb", at = @At("RETURN"))
	private void onArmorReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				amount, new ArmorModifyReason((LivingEntity)(Object)this)
		));
	}

	@Inject(
			method = "getDamageAfterMagicAbsorb",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Ljava/lang/Math;max(FF)F"
			)
	)
	private void onResistanceReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> tracker.modifyDamage(
				amount, new StatusEffectModifyReason(MobEffects.DAMAGE_RESISTANCE, this.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier())
		));
	}

	@Inject(
			method = "getDamageAfterMagicAbsorb",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterMagicAbsorb(FF)F"
			)
	)
	private void onEnchantmentReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageTracker().ifPresent(tracker -> {
			//#if MC >= 12100
			//$$ LivingEntity self = (LivingEntity)(Object)this;
			//$$ Level world = EntityUtils.getEntityWorld(self);
			//$$ if (world instanceof ServerLevel serverWorld)
			//#endif
			{
				// vanilla copy
				// ref: net.minecraft.entity.LivingEntity#applyEnchantmentsToDamage
				// notes: epf is float after mc1.21
				double epf = EnchantmentHelper.getDamageProtection(
						//#if MC >= 12100
						//$$ serverWorld,  self, source
						//#else
						this.getArmorSlots(), source
						//#endif
				);
				tracker.modifyDamage(amount, new EnchantmentModifyReason(epf));
			}
		});
	}
}
