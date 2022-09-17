package carpettisaddition.mixins.rule.undeadDontBurnInSunlight;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin
{
	@ModifyArg(
			method = "tickMovement",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/AbstractSkeletonEntity;setOnFireFor(I)V"
			)
	)
	private int undeadDontBurnInSunlight_skeleton(int duration)
	{
		if (CarpetTISAdditionSettings.undeadDontBurnInSunlight)
		{
			duration = 0;
		}
		return duration;
	}
}
