package carpettisaddition.mixins.logger.microtiming.events;

import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(ChunkTickScheduler.class)
public interface ChunkTickSchedulerAccessor
{
	@Accessor
	Queue<OrderedTick<?>> getTickQueue();
}
