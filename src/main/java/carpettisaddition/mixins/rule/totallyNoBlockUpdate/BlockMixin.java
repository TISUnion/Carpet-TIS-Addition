package carpettisaddition.mixins.rule.totallyNoBlockUpdate;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin
{
	@Inject(method = "updateNeighborStates", at = @At("HEAD"), cancellable = true)
	private void disableStateUpdateMaybe(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.totallyNoBlockUpdate)
		{
			ci.cancel();
		}
	}
}
