package carpettisaddition.mixins.rule.itemEntitySkipMovementDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin
{
	@ModifyConstant(
			method = "tick",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							target = "Lnet/minecraft/entity/ItemEntity;getId()I"
					)
			),
			constant = @Constant(intValue = 4, ordinal = 0),
			require = 0
	)
	private int itemEntitySkipMovementDisabled(int value)
	{
		if (CarpetTISAdditionSettings.itemEntitySkipMovementDisabled)
		{
			return 1;  // n % 1 == 0
		}
		return value;
	}
}
