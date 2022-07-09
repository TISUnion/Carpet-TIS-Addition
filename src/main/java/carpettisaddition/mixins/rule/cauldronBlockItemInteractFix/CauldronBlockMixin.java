package carpettisaddition.mixins.rule.cauldronBlockItemInteractFix;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.CauldronBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//#if MC >= 11500
import net.minecraft.util.ActionResult;
//#else
//$$ import org.spongepowered.asm.mixin.injection.Slice;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin
{
	//#if MC < 11700
	@Inject(
			//#if MC >= 11500
			method = "onUse",
			//#else
			//$$ method = "activate",
			//$$ slice = @Slice(
			//$$ 		from = @At(
			//$$ 				value = "FIELD",
			//$$ 				target = "Lnet/minecraft/stat/Stats;CLEAN_SHULKER_BOX:Lnet/minecraft/util/Identifier;"
			//$$ 		)
			//$$ ),
			//#endif
			at = @At(
					//#if MC >= 11500
					value = "FIELD",
					target = "Lnet/minecraft/util/ActionResult;CONSUME:Lnet/minecraft/util/ActionResult;"
					//#else
					//$$ value = "RETURN",
					//$$ ordinal = 0
					//#endif
			),
			cancellable = true
	)
	private void cauldronBlockItemInteractFix(
			//#if MC >= 11500
			CallbackInfoReturnable<ActionResult> cir
			//#else
			//$$ CallbackInfoReturnable<Boolean> cir
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.cauldronBlockItemInteractFix)
		{
			cir.setReturnValue(
					//#if MC >= 11500
					ActionResult.PASS
					//#else
					//$$ false
					//#endif
			);
		}
	}
	//#endif
}
