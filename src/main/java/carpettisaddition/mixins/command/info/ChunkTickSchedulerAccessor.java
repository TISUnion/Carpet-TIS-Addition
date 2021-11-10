package carpettisaddition.mixins.command.info;

import net.minecraft.world.tick.ChunkTickScheduler;
import net.minecraft.world.tick.OrderedTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(ChunkTickScheduler.class)
public interface ChunkTickSchedulerAccessor<T>
{
	@Accessor
	Queue<OrderedTick<T>> getTickQueue();
}
