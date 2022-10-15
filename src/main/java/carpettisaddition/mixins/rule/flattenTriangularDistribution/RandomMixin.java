package carpettisaddition.mixins.rule.flattenTriangularDistribution;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyInterface;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
@Mixin(DummyInterface.class)
public interface RandomMixin
{
}
