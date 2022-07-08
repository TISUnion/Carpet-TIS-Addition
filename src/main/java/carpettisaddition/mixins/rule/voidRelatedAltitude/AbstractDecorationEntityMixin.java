package carpettisaddition.mixins.rule.voidRelatedAltitude;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.16"))
@Mixin(AbstractDecorationEntity.class)
public abstract class AbstractDecorationEntityMixin
{
	//#if MC < 11600
	//$$ @ModifyConstant(method = "tick", constant = @Constant(doubleValue = -64.0D), allow = 1, require = 0)
	//$$ private double modifyVoidRelatedAltitude(double value)
	//$$ {
	//$$ 	return CarpetTISAdditionSettings.voidRelatedAltitude;
	//$$ }
	//#endif
}