package carpettisaddition.logging.loggers.lightqueue;

import carpet.logging.HUDLogger;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.world.chunk.light.LightingProvider;

import java.util.Deque;
import java.util.Map;

public class LightQueueLogger extends AbstractHUDLogger
{
	public static final String NAME = "lightQueue";
	private static final LightQueueLogger INSTANCE = new LightQueueLogger();
	private final Map<ServerWorld, WindowedDataRecorder> dataMap = Maps.newHashMap();
	private final Map<String, ServerWorld> nameToWorldMap = Maps.newHashMap();

	public LightQueueLogger()
	{
		super(NAME);
	}

	public static LightQueueLogger getInstance()
	{
		return INSTANCE;
	}

	public void attachServer(MinecraftServer minecraftServer)
	{
		this.dataMap.clear();
		this.nameToWorldMap.clear();
		for (ServerWorld world: minecraftServer.getWorlds())
		{
			this.dataMap.put(world, new WindowedDataRecorder());
			this.nameToWorldMap.put(TextUtil.dimensionToString(world.getRegistryKey()), world);
		}
	}

	public HUDLogger getHUDLogger()
	{
		return ExtensionLoggerRegistry.standardHUDLogger(LightQueueLogger.NAME, "dynamic", new String[]{"dynamic", "overworld", "the_nether", "the_end"});
	}

	public void tick()
	{
		this.nameToWorldMap.values().forEach(world -> {
			LightingProvider lightingProvider = world.getLightingProvider();
			if (lightingProvider instanceof IServerLightingProvider)
			{
				IServerLightingProvider iProvider = (IServerLightingProvider)lightingProvider;
				long enqueuedCount = iProvider.getEnqueuedTaskCountAndClean();
				long executedCount = iProvider.getExecutedTaskCountAndClean();
				if (ExtensionLoggerRegistry.__lightQueue)
				{
					this.dataMap.get(world).add(new RecordedData(enqueuedCount, executedCount, iProvider.getQueueSize()));
				}
				else
				{
					this.dataMap.get(world).clear();
				}
			}
		});
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		if (!(playerEntity.getEntityWorld() instanceof ServerWorld))
		{
			return new BaseText[]{Messenger.s("not ServerWorld")};
		}
		ServerWorld serverWorld = this.nameToWorldMap.getOrDefault(option, (ServerWorld)playerEntity.getEntityWorld());
		WindowedDataRecorder recorder = this.dataMap.get(serverWorld);
		Deque<RecordedData> deque = recorder.getQueue();

		long enqueuedCount = recorder.getEnqueuedCount();
		long executedCount = recorder.getExecutedCount();
		long queueSize = deque.isEmpty() ? 0 : deque.getLast().queueSize;
		double enqueueSpeed = (double)enqueuedCount / deque.size();
		double executeSpeed = (double)executedCount / deque.size();
		double increaseSpeed = enqueueSpeed - executeSpeed;

		BaseText header = Messenger.c(
				"g LQ(",
				TextUtil.getColoredDimensionSymbol(serverWorld.getRegistryKey()),
				"g ) "
		);
		return new BaseText[]{
				Messenger.c(
						header,
						String.format("%s%.1f", increaseSpeed >= 0 ? "e +" : "n ", increaseSpeed),
						"g /gt",
						"g  S: ",
						String.format("q %d", queueSize),
						"g  T: ",
						String.format("p %.1f", executeSpeed > 0 ? queueSize / executeSpeed : 0.0F),
						"g gt"
				),
				Messenger.c(
						"g Light I",
						"f /",
						"g O",
						"f : ",
						String.format("g %.1f", enqueueSpeed),
						"f /",
						String.format("g %.1f", executeSpeed)
				)
		};
	}
}
