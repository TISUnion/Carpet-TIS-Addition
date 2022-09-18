package carpettisaddition.mixins.logger.microtiming.events.tiletick;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18"))
@Mixin(ChunkTickScheduler.class)
public interface ChunkTickSchedulerAccessor
{
	@Accessor
	Queue<OrderedTick<?>> getTickQueue();
}