package carpettisaddition.mixins.rule.voidRelatedAltitude;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.17"))
@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityMixin
{
	//#if MC < 11700
	@ModifyConstant(method = "tick", constant = @Constant(doubleValue = -64.0D), allow = 1, require = 0)
	private double modifyVoidRelatedAltitude(double value)
	{
		return CarpetTISAdditionSettings.voidRelatedAltitude;
	}
	//#endif
}
