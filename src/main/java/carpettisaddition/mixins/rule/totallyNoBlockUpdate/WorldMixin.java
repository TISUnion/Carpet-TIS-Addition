package carpettisaddition.mixins.rule.totallyNoBlockUpdate;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(World.class)
public abstract class WorldMixin
{
	@Inject(method = "updateNeighbor", at = @At("HEAD"), cancellable = true)
	private void disableBlockUpdateMaybe(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.totallyNoBlockUpdate)
		{
			ci.cancel();
		}
	}
}
