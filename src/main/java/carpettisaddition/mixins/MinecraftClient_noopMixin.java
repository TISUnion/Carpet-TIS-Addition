package carpettisaddition.mixins;

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClient_noopMixin
{
    // this is here just to load the CarpetTISAdditionServer class, otherwise noone would load it / need it
    // if you have already you own mixins that use your extension class in any shape or form
    // you don't need this one
    // This one is for the client
    @Inject(method = "<init>",at = @At("RETURN"))
    private void loadMe(CallbackInfo ci)
    {
        CarpetTISAdditionServer.noop();
    }
}
