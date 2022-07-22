package carpettisaddition.mixins.logger.microtiming.tickstages.spawning;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(DummyClass.class)
public abstract class WanderingTraderManagerMixin
{
	// wandering trader spawning is no longer a special individual phase in 1.16+
}
