package carpettisaddition.mixins.rule.entityTrackerDistance;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin
{
	@Inject(method = "getMaxTrackDistance", at = @At("HEAD"), cancellable = true)
	private void overrideEntityTrackerDistance(CallbackInfoReturnable<Integer> cir)
	{
		if (CarpetTISAdditionSettings.entityTrackerDistance > 0)
		{
			cir.setReturnValue(CarpetTISAdditionSettings.entityTrackerDistance);
		}
	}
}
