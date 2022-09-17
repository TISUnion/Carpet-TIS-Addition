package carpettisaddition.mixins.rule.undeadDontBurnInSunlight;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin
{
	@ModifyArg(
			method = "tickMovement",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/mob/ZombieEntity;setOnFireFor(I)V"
			)
	)
	private int undeadDontBurnInSunlight_zombie(int duration)
	{
		if (CarpetTISAdditionSettings.undeadDontBurnInSunlight)
		{
			duration = 0;
		}
		return duration;
	}
}
