package carpettisaddition.mixins.core;

import carpettisaddition.CarpetTISAdditionClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
	@Inject(method = "tick", at = @At("RETURN"))
	private void clientTickHook$TISCM(CallbackInfo ci)
	{
		CarpetTISAdditionClient.getInstance().onClientTick((MinecraftClient)(Object)this);
	}
}
