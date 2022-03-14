package carpettisaddition.mixins.rule.entityTrackerInterval;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin
{
	@Inject(method = "getTrackTickInterval", at = @At("HEAD"), cancellable = true)
	private void overrideEntityTrackerInterval(CallbackInfoReturnable<Integer> cir)
	{
		if (CarpetTISAdditionSettings.entityTrackerInterval > 0)
		{
			cir.setReturnValue(CarpetTISAdditionSettings.entityTrackerInterval);
		}
	}
}
