package carpettisaddition.mixins.rule.voidRelatedAltitude;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin
{
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = -64.0D), allow = 1, require = 0)
	private double modifyVoidRelatedAltitude(double value)
	{
		return CarpetTISAdditionSettings.voidRelatedAltitude;
	}
}
