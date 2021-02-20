package carpettisaddition.logging.loggers.lightqueue;

import carpet.logging.HUDLogger;
import carpet.utils.Messenger;
import carpettisaddition.logging.ExtensionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.BaseText;
import net.minecraft.util.Tickable;
import net.minecraft.world.chunk.light.LightingProvider;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class LightQueueLogger extends AbstractLogger implements Tickable
{
	public static final String NAME = "lightQueue";
	private static final int SAMPLING_DURATION = 20;
	private static final LightQueueLogger INSTANCE = new LightQueueLogger();
	private final Map<ServerWorld, WindowedDataRecorder> recorderMap = Maps.newHashMap();
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
		this.recorderMap.clear();
		this.nameToWorldMap.clear();
		for (ServerWorld world: minecraftServer.getWorlds())
		{
			this.recorderMap.put(world, new WindowedDataRecorder(SAMPLING_DURATION));
			String[] s = world.getDimension().getType().toString().split(" ");
			this.nameToWorldMap.put(s[Math.max(s.length - 1, 0)], world);
		}
		System.err.println("world count " + this.recorderMap.size());
	}

	public HUDLogger getHUDLogger()
	{
		List<String> worldNameList = new ArrayList<>(this.nameToWorldMap.keySet());
		worldNameList.add("dynamic");
		return ExtensionLoggerRegistry.standardHUDLogger(LightQueueLogger.NAME, "dynamic", worldNameList.toArray(new String[0]));
	}

	@Override
	public void tick()
	{
		this.recorderMap.forEach((world, recorder) -> {
			LightingProvider lightingProvider = world.getLightingProvider();
			if (lightingProvider instanceof IServerLightingProvider)
			{
				IServerLightingProvider iProvider = (IServerLightingProvider)lightingProvider;
				recorder.add(new RecordedData(iProvider.getEnqueuedTaskCount(), iProvider.getExecutedTaskCount(), iProvider.getTaskQueue().size()));
			}
		});
	}

	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		if (!(playerEntity.getEntityWorld() instanceof ServerWorld))
		{
			return new BaseText[]{Messenger.s("not ServerWorld")};
		}
		ServerWorld serverWorld = this.nameToWorldMap.getOrDefault(option, (ServerWorld)playerEntity.getEntityWorld());
		WindowedDataRecorder recorder = this.recorderMap.get(serverWorld);
		Deque<RecordedData> innerQueue = recorder.getQueue();
		if (innerQueue.isEmpty())
		{
			return new BaseText[]{Messenger.s("queue empty")};
		}
		RecordedData lastTickData = innerQueue.getLast();
		double increaseSpeed = (double)(recorder.getEnqueuedCount() - recorder.getExecutedCount()) / innerQueue.size();
		return new BaseText[]{Messenger.c(
				"w Light Queue",
				String.format("w  I/O/S: %d/%d/%d", recorder.getEnqueuedCount(), recorder.getExecutedCount(), lastTickData.queueSize),
				String.format("w  %s%.1f/gt", increaseSpeed >= 0.0D ? "+" : "-", increaseSpeed)
		)};
	}
}
