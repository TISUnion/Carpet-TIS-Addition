package carpettisaddition.mixins.command.info.compat.lithium;

import carpettisaddition.commands.info.QueueAccessibleChunkTickScheduler;
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.compat.DummyClass;
import carpettisaddition.utils.mixin.testers.LithiumEntityWorldTickSchedulerTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"),
		@Condition(type = Condition.Type.TESTER, tester = LithiumEntityWorldTickSchedulerTester.class)
})
@Mixin(DummyClass.class)
public abstract class ChunkTickSchedulerMixin<T> implements QueueAccessibleChunkTickScheduler<T>
{
}