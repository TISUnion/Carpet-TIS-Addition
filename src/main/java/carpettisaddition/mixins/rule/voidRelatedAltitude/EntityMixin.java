package carpettisaddition.mixins.rule.voidRelatedAltitude;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * 1.17+ mixin is handled in the version-specified class
 */
@Mixin(Entity.class)
public abstract class EntityMixin
{
	@ModifyConstant(method = "baseTick", constant = @Constant(doubleValue = -64.0D), allow = 1, require = 0)
	private double modifyVoidRelatedAltitude(double value)
	{
		return CarpetTISAdditionSettings.voidRelatedAltitude;
	}
}
