package carpettisaddition.mixins.command.info.entity;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric carpet removed the "/info entity" command tree branch in mc 1.17+
 * so we need to manually mixin and make a new one
 */
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(DummyClass.class)
public abstract class InfoCommandMixin
{
}