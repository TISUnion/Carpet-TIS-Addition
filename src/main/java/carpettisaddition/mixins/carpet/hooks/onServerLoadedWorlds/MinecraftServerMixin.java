package carpettisaddition.mixins.carpet.hooks.onServerLoadedWorlds;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Inject(method = "loadWorld", at = @At("TAIL"))
	private void onSetupServerIntegrated(CallbackInfo ci)
	{
		CarpetTISAdditionServer.INSTANCE.onServerLoadedWorldsCTA((MinecraftServer) (Object) this);
	}
}