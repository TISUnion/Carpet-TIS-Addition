package carpettisaddition.mixins.rule.totallyNoBlockUpdate;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin
{
	@Inject(method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V", at = @At("HEAD"), cancellable = true)
	private void disableStateUpdateMaybe(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.totallyNoBlockUpdate)
		{
			ci.cancel();
		}
	}
}
