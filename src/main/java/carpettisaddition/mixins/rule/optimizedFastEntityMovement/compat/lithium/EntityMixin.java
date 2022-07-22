package carpettisaddition.mixins.rule.optimizedFastEntityMovement.compat.lithium;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import carpettisaddition.utils.mixin.testers.LithiumEntityMovementOptimizationTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"),
		@Condition(type = Condition.Type.TESTER, tester = LithiumEntityMovementOptimizationTester.class)
})
@Mixin(value = DummyClass.class, priority = 2000)
public abstract class EntityMixin
{
}