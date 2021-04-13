package carpettisaddition.mixins.rule.lightEngineMaxBatchSize;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerLightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLightingProvider.class)
public abstract class ServerLightingProviderMixin
{
	@Shadow public abstract void setTaskBatchSize(int taskBatchSize);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void adjustBatchSize(CallbackInfo ci)
	{
		this.setTaskBatchSize(CarpetTISAdditionSettings.lightEngineMaxBatchSize);
	}
}
