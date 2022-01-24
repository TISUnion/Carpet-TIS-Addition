package carpettisaddition.mixins.carpet.events.onServerLoadedWorlds;

import carpettisaddition.CarpetTISAdditionServer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin
{
	@Inject(method = "loadWorld", at = @At("TAIL"))
	private void onSetupServerIntegrated(CallbackInfo ci)
	{
		CarpetTISAdditionServer.INSTANCE.onServerLoadedWorldsCTA((IntegratedServer) (Object) this);
	}
}
