package carpettisaddition.mixins.carpet.hooks;

import carpet.CarpetServer;
import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CarpetServer.class)
public abstract class CarpetServerMixin
{
	/**
	 * We need to steal the MinecraftServer object fast enough, and {@link carpet.CarpetExtension#onServerLoaded} is too late
	 * <p>
	 * Reason: We need to access our {@link CarpetTISAdditionServer#minecraft_server} in those rule validator logics
	 * when carpet invokes the {@link carpet.settings.SettingsManager#attachServer} in {@link CarpetServer#onServerLoaded}
	 */
	@Inject(method = "onServerLoaded", at = @At("HEAD"), remap = false)
	private static void stealMinecraftServerObjectFast$TISCM(MinecraftServer server, CallbackInfo ci)
	{
		CarpetTISAdditionServer.minecraft_server = server;
	}
}
