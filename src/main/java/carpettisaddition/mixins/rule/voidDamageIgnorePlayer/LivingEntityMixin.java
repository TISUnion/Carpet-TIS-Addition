package carpettisaddition.mixins.rule.voidDamageIgnorePlayer;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
	@SuppressWarnings("ConstantConditions")
	@Inject(method = "destroy", at = @At("HEAD"), cancellable = true)
	private void voidDamageAmount(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.voidDamageIgnorePlayer)
		{
			if ((Object)this instanceof PlayerEntity)
			{
				ci.cancel();
			}
		}
	}
}
