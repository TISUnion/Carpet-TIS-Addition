package carpettisaddition.mixins.carpet;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


public class OnServerLoadedWorldsMixins
{
	@Mixin(MinecraftServer.class)
	public static class MinecraftServerMixins
	{
		@Inject(method = "loadWorld", at = @At("TAIL"))
		private void onSetupServerIntegrated(CallbackInfo ci)
		{
			CarpetTISAdditionServer.instance.onServerLoadedWorldsCTA((IntegratedServer) (Object) this);
		}
	}

	@Mixin(IntegratedServer.class)
	public static class IntegratedServerMixins
	{
		@Inject(method = "loadWorld", at = @At("TAIL"))
		private void onSetupServerIntegrated(CallbackInfo ci)
		{
			CarpetTISAdditionServer.instance.onServerLoadedWorldsCTA((IntegratedServer) (Object) this);
		}
	}
}
