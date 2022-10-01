package carpettisaddition.mixins.core.client;

import carpettisaddition.CarpetTISAdditionClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClientGame.class)
public abstract class MinecraftClientGameMixin
{
	@Shadow @Final private MinecraftClient client;

	@Inject(method = "onLeaveGameSession", at = @At("TAIL"))
	private void onClientDisconnected(CallbackInfo ci)
	{
		CarpetTISAdditionClient.getInstance().onClientDisconnected(this.client);
	}
}
