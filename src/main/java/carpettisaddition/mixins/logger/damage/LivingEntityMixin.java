package carpettisaddition.mixins.logger.damage;

import carpettisaddition.logging.loggers.damage.DamageLogger;
import carpettisaddition.logging.loggers.damage.interfaces.ILivingEntity;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntity
{
	@Shadow protected float lastDamageTaken;

	@Shadow public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

	@Shadow public abstract Iterable<ItemStack> getArmorItems();

	private DamageLogger damageLogger;

	@Override
	public Optional<DamageLogger> getDamageLogger()
	{
		return Optional.ofNullable(damageLogger);
	}

	@Override
	public void setDamageLogger(DamageLogger damageLogger)
	{
		this.damageLogger = damageLogger;
	}

	// at the start of general damage calculation
	@Inject(
			method = "damage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z"
			)
	)
	void onDamageStarted(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
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
	void onHelmetReducedAnvilDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		this.getDamageLogger().ifPresent(damageLogger -> damageLogger.modifyDamage(amount * 0.75F, ModifyReason.HELMET));
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
					value = "INVOKE",
					target = "Lnet/minecraft/entity/damage/DamageSource;isProjectile()Z",
					ordinal = 0
			)
	)
	void onShieldReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		this.getDamageLogger().ifPresent(damageLogger -> damageLogger.modifyDamage(amount, ModifyReason.SHIELD));
	}

	@Inject(
			method = "damage",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/entity/LivingEntity;lastDamageTaken:F",
					ordinal = 0
			)
	)
	void onRecentHintReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir)
	{
		this.getDamageLogger().ifPresent(damageLogger -> damageLogger.modifyDamage(
				Math.max(amount - this.lastDamageTaken, 0.0F), ModifyReason.RECENTLY_HIT
		));
	}

	@Inject(method = "applyArmorToDamage", at = @At("RETURN"))
	void onArmorReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageLogger().ifPresent(damageLogger -> damageLogger.modifyDamage(
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
	void onResistanceReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageLogger().ifPresent(damageLogger -> damageLogger.modifyDamage(
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
	void onEnchantmentReducedDamage(DamageSource source, float amount, CallbackInfoReturnable<Float> cir)
	{
		this.getDamageLogger().ifPresent(damageLogger -> {
			int epf = EnchantmentHelper.getProtectionAmount(this.getArmorItems(), source);  // vanilla copy
			damageLogger.modifyDamage(amount, new EnchantmentModifyReason(epf));
		});
	}
}
