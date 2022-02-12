package carpettisaddition.mixins.rule.cauldronBlockItemInteractFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.CauldronBlock;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin
{
	@Inject(
			method = "onUse",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/util/ActionResult;CONSUME:Lnet/minecraft/util/ActionResult;"
			),
			cancellable = true
	)
	private void cauldronBlockItemInteractFix(CallbackInfoReturnable<ActionResult> cir)
	{
		if (CarpetTISAdditionSettings.cauldronBlockItemInteractFix)
		{
			cir.setReturnValue(ActionResult.PASS);
		}
	}
}
