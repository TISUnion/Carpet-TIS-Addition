package carpettisaddition.mixins.carpet.hooks;

import carpet.logging.HUDController;
import carpettisaddition.logging.TISAdditionHUDController;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11600
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import java.util.List;
//$$ import java.util.function.Consumer;
//#else
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

/**
 * Using mixin instead of directly invoking HUDController#register should have better carpet version compatibility,
 * since HUDController#register is not a static method, which doesn't seem to be intentional and might gets changed
 * in the future version
 */
@Mixin(HUDController.class)
public abstract class HUDControllerMixin
{
	//#if MC >= 11600
	//$$
	//$$ @Shadow(remap = false)
	//$$ private static List<Consumer<MinecraftServer>> HUDListeners;
	//$$
	//$$ static
	//$$ {
	//$$ 	HUDListeners.add(TISAdditionHUDController::updateHUD);
	//$$ }
	//#else
	@Inject(
			method = "update_hud",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Map;keySet()Ljava/util/Set;"
			),
			remap = false
	)
	private static void updateTISAdditionHUDLoggers(MinecraftServer server, CallbackInfo ci)
	{
		TISAdditionHUDController.updateHUD(server);
	}
	//#endif
}
