package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.entity.decoration.EnderCrystalEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EndCrystalItem.class)
public abstract class EndCrystalItemMixin
{
	@ModifyVariable(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/entity/decoration/EndCrystalEntity;setShowBottom(Z)V"
					//#else
					target = "Lnet/minecraft/entity/decoration/EnderCrystalEntity;setShowBottom(Z)V"
					//#endif
			)
	)
	private EnderCrystalEntity preciseEntityPlacement(EnderCrystalEntity enderCrystalEntity, ItemUsageContext context)
	{
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.adjustEntity(enderCrystalEntity, context);
		}
		return enderCrystalEntity;
	}
}
