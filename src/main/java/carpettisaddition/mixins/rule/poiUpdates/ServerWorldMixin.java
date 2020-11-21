package carpettisaddition.mixins.rule.poiUpdates;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Inject(method = "onBlockChanged", at = @At("HEAD"), cancellable = true)
	void disablePOIUpdates(CallbackInfo ci)
	{
		if (!CarpetTISAdditionSettings.poiUpdates)
		{
			ci.cancel();
		}
	}
}
