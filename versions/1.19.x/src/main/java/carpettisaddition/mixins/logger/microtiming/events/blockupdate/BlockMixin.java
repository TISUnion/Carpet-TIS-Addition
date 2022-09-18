package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Traditional block / state update stuffs, before mc 1.19
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.19"))
@Mixin(DummyClass.class)
public abstract class BlockMixin
{
}
