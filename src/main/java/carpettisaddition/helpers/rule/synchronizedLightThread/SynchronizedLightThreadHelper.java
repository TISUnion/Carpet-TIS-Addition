package carpettisaddition.helpers.rule.synchronizedLightThread;

import carpet.utils.CarpetProfiler;
import carpettisaddition.mixins.rule.synchronizedLightThread.ServerLightingProviderAccessor;
import carpettisaddition.mixins.rule.synchronizedLightThread.TaskExecutorAccessor;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.thread.TaskQueue;
import net.minecraft.world.chunk.light.LightingProvider;

public class SynchronizedLightThreadHelper
{
	public static final String SECTION_NAME = "Lighting synchronization";

	public static void waitForLightThread(ServerWorld serverWorld)
	{
		serverWorld.getProfiler().push(SECTION_NAME);
		CarpetProfiler.ProfilerToken token = CarpetProfiler.start_section(serverWorld, SECTION_NAME, CarpetProfiler.TYPE.GENERAL);

		LightingProvider lightingProvider = serverWorld.getLightingProvider();
		if (lightingProvider instanceof ServerLightingProvider)
		{
			// the task queue of the executor of the light thread
			TaskQueue<?, ?> queue = ((TaskExecutorAccessor<?>)((ServerLightingProviderAccessor)lightingProvider).getProcessor()).getQueue();
			while (!queue.isEmpty())
			{
				Thread.yield();
			}
		}

		serverWorld.getProfiler().pop();
		CarpetProfiler.end_current_section(token);
	}
}
