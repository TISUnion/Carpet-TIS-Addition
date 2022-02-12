package carpettisaddition.mixins.rule.cauldronBlockItemInteractFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.CauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin
{
	@Inject(
			method = "activate",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							target = "Lnet/minecraft/stat/Stats;CLEAN_SHULKER_BOX:Lnet/minecraft/util/Identifier;"
					)
			),
			at = @At(
					value = "RETURN",
					ordinal = 0
			),
			cancellable = true
	)
	private void cauldronBlockItemInteractFix(CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetTISAdditionSettings.cauldronBlockItemInteractFix)
		{
			cir.setReturnValue(false);
		}
	}
}
