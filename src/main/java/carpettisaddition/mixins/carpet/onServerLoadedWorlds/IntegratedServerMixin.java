package carpettisaddition.mixins.carpet.onServerLoadedWorlds;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin
{
	@Inject(method = "loadWorld", at = @At("TAIL"))
	private void onSetupServerIntegrated(CallbackInfo ci)
	{
		CarpetTISAdditionServer.instance.onServerLoadedWorldsCTA((IntegratedServer) (Object) this);
	}
}
