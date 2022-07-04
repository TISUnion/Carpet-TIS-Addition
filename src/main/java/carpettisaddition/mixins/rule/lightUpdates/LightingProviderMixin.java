package carpettisaddition.mixins.rule.lightUpdates;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightingProvider.class)
public class LightingProviderMixin {

    /**
     * Need reduce your CPU computing? Just skip all the calculate process and make the result always be 15!
     */
    @Inject(method = "doLightUpdates", at = @At(value = "HEAD"), cancellable = true)
    private void doLightUpdates(int i, boolean doSkylight, boolean skipEdgeLightPropagation, CallbackInfoReturnable<Integer> cir){
        if(CarpetTISAdditionSettings.lightUpdates.isMinCalculateCost()) {
            cir.setReturnValue(15);
        }
    }
}
