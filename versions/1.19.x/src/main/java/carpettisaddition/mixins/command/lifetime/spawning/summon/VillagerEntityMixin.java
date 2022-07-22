package carpettisaddition.mixins.command.lifetime.spawning.summon;

import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.19"))
@Mixin(DummyClass.class)
public abstract class VillagerEntityMixin
{
	// village summons iron golem thing is moved into LargeEntitySpawnHelper class
}
