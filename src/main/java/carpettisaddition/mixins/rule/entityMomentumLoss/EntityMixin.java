package carpettisaddition.mixins.rule.entityMomentumLoss;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@ModifyConstant(method = "fromTag", constant = @Constant(doubleValue = 10.0D), require = 3)
	private double dontYeetItsSpeed(double value)
	{
		if (!CarpetTISAdditionSettings.entityMomentumLoss)
		{
			return Double.MAX_VALUE;
		}
		// vanilla, should be 10.0D
		return value;
	}
}
