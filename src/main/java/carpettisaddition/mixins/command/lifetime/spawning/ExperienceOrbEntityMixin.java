package carpettisaddition.mixins.command.lifetime.spawning;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"))
@Mixin(DummyClass.class)
public abstract class ExperienceOrbEntityMixin
{
	// injection to the static method for xp orb creation in 1.17
}