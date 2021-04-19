package carpettisaddition.mixins.carpet.hooks;

import carpet.logging.HUDController;
import carpettisaddition.logging.TISAdditionHUDController;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.function.Consumer;

/**
 * Using mixin instead of directly invoking HUDController#register should have better carpet version compatibility,
 * since HUDController#register is not a static method, which doesn't seem to be intentional and might gets changed
 * in the future version
 */
@Mixin(HUDController.class)
public abstract class HUDControllerMixin
{
	@Shadow(remap = false)
	private static List<Consumer<MinecraftServer>> HUDListeners;

	static
	{
		HUDListeners.add(TISAdditionHUDController::updateHUD);
	}
}
