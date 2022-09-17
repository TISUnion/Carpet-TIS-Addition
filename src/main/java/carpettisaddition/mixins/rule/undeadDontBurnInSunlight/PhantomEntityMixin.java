package carpettisaddition.mixins.rule.undeadDontBurnInSunlight;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.mob.PhantomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PhantomEntity.class)
public abstract class PhantomEntityMixin
{
	@ModifyArg(
			method = "tickMovement",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/PhantomEntity;setOnFireFor(I)V"
			)
	)
	private int undeadDontBurnInSunlight_phantom(int duration)
	{
		if (CarpetTISAdditionSettings.undeadDontBurnInSunlight)
		{
			duration = 0;
		}
		return duration;
	}
}
