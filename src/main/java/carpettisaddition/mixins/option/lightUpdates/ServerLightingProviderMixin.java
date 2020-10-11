package carpettisaddition.mixins.option.lightUpdates;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.world.ServerLightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerLightingProvider.class)
public abstract class ServerLightingProviderMixin
{
	@Inject(
			method = "enqueue(IILjava/util/function/IntSupplier;Lnet/minecraft/server/world/ServerLightingProvider$Stage;Ljava/lang/Runnable;)V",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	void onAddingLightUpdateTask(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.lightUpdates == CarpetTISAdditionSettings.LightUpdateOptions.OFF)
		{
			ci.cancel();
		}
	}

	@Inject(
			method = "runTasks",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	void onExecutingLightUpdates(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.lightUpdates != CarpetTISAdditionSettings.LightUpdateOptions.ON)  // off or suppressed
		{
			ci.cancel();
		}
	}
}
