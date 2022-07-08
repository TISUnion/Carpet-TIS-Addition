package carpettisaddition.mixins.rule.totallyNoBlockUpdate;

import carpettisaddition.CarpetTISAdditionSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11600
//$$ import net.minecraft.block.AbstractBlock;
//#else
import net.minecraft.block.Block;
//#endif

@Mixin(
		//#if MC >= 11600
		//$$ AbstractBlock.AbstractBlockState.class
		//#else
		Block.class
		//#endif
)
public abstract class BlockMixin
{
	@Inject(
			//#if MC >= 11600
			//$$ method = "updateNeighbors(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;II)V",
			//#else
			method = "updateNeighborStates",
			//#endif
			at = @At("HEAD"),
			cancellable = true
	)
	private void disableStateUpdateMaybe(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.totallyNoBlockUpdate)
		{
			ci.cancel();
		}
	}
}
