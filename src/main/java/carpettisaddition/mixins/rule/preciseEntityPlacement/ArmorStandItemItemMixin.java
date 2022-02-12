package carpettisaddition.mixins.rule.preciseEntityPlacement;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.preciseEntityPlacement.PreciseEntityPlacer;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.ItemUsageContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ArmorStandItem.class)
public abstract class ArmorStandItemItemMixin
{
	@ModifyVariable(
			method = "useOnBlock",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;refreshPositionAndAngles(DDDFF)V",
					shift = At.Shift.AFTER
			)
	)
	private ArmorStandEntity preciseEntityPlacement(ArmorStandEntity armorStandEntity, ItemUsageContext context)
	{
		if (CarpetTISAdditionSettings.preciseEntityPlacement)
		{
			PreciseEntityPlacer.adjustEntity(armorStandEntity, context);
		}
		return armorStandEntity;
	}
}
