package carpettisaddition.mixins.carpet.tweaks.rule.creativeNoClip;

//#if MC >= 11500
import carpet.CarpetSettings;
//#else
//$$ import carpettisaddition.utils.compat.carpet.CarpetSettings;
//#endif

import carpettisaddition.helpers.carpet.tweaks.rule.creativeNoClip.CreativeNoClipHelper;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPredicates.class)
public abstract class EntityPredicatesMixin
{
	/**
	 * The lambda method with the declaration of {@link EntityPredicates#EXCEPT_SPECTATOR}
	 */
	@Dynamic
	@Inject(
			//#if MC >= 11700
			//$$ method = "method_24517",
			//#else
			method = "method_5907",
			//#endif
			at = @At("TAIL"),
			remap = false,
			cancellable = true
	)
	private static void creativeNoClipEnhancement(Entity entity, CallbackInfoReturnable<Boolean> cir)
	{
		if (CarpetSettings.creativeNoClip && CreativeNoClipHelper.ignoreNoClipPlayersFlag.get())
		{
			cir.setReturnValue(cir.getReturnValue() && !CreativeNoClipHelper.isNoClipPlayer(entity));
		}
	}
}
