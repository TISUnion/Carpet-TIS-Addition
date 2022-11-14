package carpettisaddition.mixins.rule.toughWitherRose;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.WitherRoseBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WitherRoseBlock.class)
public abstract class WitherRoseBlockMixin
{
	@Inject(method = "canPlantOnTop", at = @At("HEAD"), cancellable = true)
	private void toughWitherRouse(CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.toughWitherRose)
		{
			cir.setReturnValue(true);
		}
	}
}
