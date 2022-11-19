package carpettisaddition.mixins.command.info.compat.lithium;

import carpettisaddition.commands.info.QueueAccessibleChunkTickScheduler;
import carpettisaddition.utils.ModIds;
import carpettisaddition.utils.ReflectionUtil;
import carpettisaddition.utils.mixin.testers.LithiumEntityWorldTickSchedulerTester;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Queue;

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"),
		@Condition(type = Condition.Type.TESTER, tester = LithiumEntityWorldTickSchedulerTester.class)
})
@Mixin(ChunkTickScheduler.class)
public abstract class ChunkTickSchedulerMixin<T> implements QueueAccessibleChunkTickScheduler<T>
{
	@SuppressWarnings("unchecked")
	@Override
	public Queue<OrderedTick<T>> getTickQueue$TISCM()
	{
		return (Queue<OrderedTick<T>>)ReflectionUtil.getField(this, "nextTickQueue").orElse(null);
	}
}