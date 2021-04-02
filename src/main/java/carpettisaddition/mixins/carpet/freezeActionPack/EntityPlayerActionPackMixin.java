package carpettisaddition.mixins.carpet.freezeActionPack;

import carpet.helpers.EntityPlayerActionPack;
import carpet.helpers.TickSpeed;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerActionPack.class)
public abstract class EntityPlayerActionPackMixin
{
	@Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
	private void stopUpdatingWhenTickFrozen(CallbackInfo ci)
	{
		if (!TickSpeed.process_entities)
		{
			ci.cancel();
		}
	}
}
