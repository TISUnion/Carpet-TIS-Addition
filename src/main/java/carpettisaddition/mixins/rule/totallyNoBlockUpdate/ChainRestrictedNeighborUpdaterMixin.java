package carpettisaddition.mixins.rule.totallyNoBlockUpdate;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

//#if MC >= 11900
//$$ import carpettisaddition.CarpetTISAdditionSettings;
//$$ import net.minecraft.world.block.ChainRestrictedNeighborUpdater;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#else
import carpettisaddition.utils.compat.DummyClass;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.19"))
@Mixin(
		//#if MC >= 11900
		//$$ ChainRestrictedNeighborUpdater.class
		//#else
		DummyClass.class
		//#endif
)
public abstract class ChainRestrictedNeighborUpdaterMixin
{
	//#if MC >= 11900
	//$$ @Inject(method = "enqueue", at = @At("HEAD"), cancellable = true)
	//$$ private void totallyNoBlockUpdate(CallbackInfo ci)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.totallyNoBlockUpdate)
	//$$ 	{
	//$$ 		ci.cancel();
	//$$ 	}
	//$$ }
	//#endif
}
