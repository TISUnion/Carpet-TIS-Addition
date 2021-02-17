package carpettisaddition.logging.loggers.lightqueue;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.mixins.logger.lightqueue.ServerLightingProviderAccessor;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.server.world.ServerLightingProvider;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Tickable;
import net.minecraft.world.chunk.light.LightingProvider;

import java.util.Map;
import java.util.Queue;

public class LightQueueLogger extends AbstractLogger implements Tickable
{
	public static final String NAME = "lightQueue";
	private static final int SAMPLING_DURATION = 20 * 60 * 5;
	private static final LightQueueLogger INSTANCE = new LightQueueLogger();
	private final Map<ServerWorld, Queue<RecordedData>> samplingResult = Maps.newHashMap();

	public LightQueueLogger()
	{
		super(NAME);
		for (ServerWorld world: CarpetTISAdditionServer.minecraft_server.getWorlds())
		{
			this.samplingResult.put(world, Queues.newArrayDeque());
		}
	}

	public static LightQueueLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void tick()
	{
		this.samplingResult.forEach((world, dataQueue) -> {
			LightingProvider lightingProvider = world.getLightingProvider();
			if (lightingProvider instanceof ServerLightingProvider)
			{
				ObjectList<Pair<ServerLightingProvider.Stage, Runnable>> taskQueue = ((ServerLightingProviderAccessor)lightingProvider).getPendingTasks();

			}
		});
	}

	private static class RecordedData
	{
		private final long enqueuedTask;
		private final long finishedTask;
		private final int queueSize;

		public RecordedData(long enqueuedTask, long finishedTask, int queueSize)
		{
			this.enqueuedTask = enqueuedTask;
			this.finishedTask = finishedTask;
			this.queueSize = queueSize;
		}
	}
}
