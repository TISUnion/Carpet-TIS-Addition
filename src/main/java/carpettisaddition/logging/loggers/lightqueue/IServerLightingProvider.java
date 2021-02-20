package carpettisaddition.logging.loggers.lightqueue;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.server.world.ServerLightingProvider;

public interface IServerLightingProvider
{
	long getEnqueuedTaskCount();

	long getExecutedTaskCount();

	ObjectList<Pair<ServerLightingProvider.Stage, Runnable>> getTaskQueue();
}
