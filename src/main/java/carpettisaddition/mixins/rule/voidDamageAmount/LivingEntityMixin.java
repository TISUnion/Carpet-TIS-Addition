package carpettisaddition.mixins.rule.voidDamageAmount;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@ModifyArg(
			method = "destroy",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
			)
	)
	private float voidDamageAmount(float amount)
	{
		if (CarpetTISAdditionSettings.voidDamageAmount != CarpetTISAdditionSettings.VANILLA_VOID_DAMAGE_AMOUNT)
		{
			amount = (float)CarpetTISAdditionSettings.voidDamageAmount;
		}
		return amount;
	}
}
