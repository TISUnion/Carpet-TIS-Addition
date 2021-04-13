package carpettisaddition.mixins.rule.synchronizedLightThread;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.synchronizedLightThread.LightThreadSynchronizer;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Inject(method = "tick", at = @At("HEAD"))
	private void onServerWorldFinishedTicking(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.synchronizedLightThread)
		{
			LightThreadSynchronizer.waitForLightThread((ServerWorld)(Object)this);
		}
	}
}
