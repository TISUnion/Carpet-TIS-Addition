package carpettisaddition.commands.info;

import net.minecraft.world.tick.OrderedTick;

import java.util.Queue;

public interface QueueAccessibleChunkTickScheduler<T>
{
	Queue<OrderedTick<T>> getTickQueue$TISCM();
}
