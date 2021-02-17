package carpettisaddition.mixins.rule.lightUpdates;

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
		//noinspection SwitchStatementWithTooFewBranches
		switch (CarpetTISAdditionSettings.lightUpdates)
		{
			case OFF:
				ci.cancel();
				break;
		}
	}

	@Inject(
			method = "runTasks",
			at = @At(value = "HEAD"),
			cancellable = true
	)
	void onExecutingLightUpdates(CallbackInfo ci)
	{
		switch (CarpetTISAdditionSettings.lightUpdates)
		{
			case OFF:
			case SUPPRESSED:
				ci.cancel();
				break;
		}
	}
}
