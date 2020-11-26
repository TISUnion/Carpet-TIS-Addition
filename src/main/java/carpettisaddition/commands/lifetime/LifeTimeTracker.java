package carpettisaddition.commands.lifetime;

import carpet.utils.Messenger;
import carpettisaddition.commands.AbstractTracker;
import carpettisaddition.commands.lifetime.interfaces.IServerWorld;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.commands.lifetime.utils.SpecificDetailMode;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class LifeTimeTracker extends AbstractTracker
{
	private static boolean attachedServer = false;
	private static final LifeTimeTracker INSTANCE = new LifeTimeTracker();

	private final Map<ServerWorld, LifeTimeWorldTracker> trackers = new Reference2ObjectArrayMap<>();

	public LifeTimeTracker()
	{
		super("LifeTime");
	}

	public static LifeTimeTracker getInstance()
	{
		return INSTANCE;
	}

	public static void attachServer(MinecraftServer minecraftServer)
	{
		attachedServer = true;
		INSTANCE.trackers.clear();
		for (ServerWorld world : minecraftServer.getWorlds())
		{
			INSTANCE.trackers.put(world, ((IServerWorld)world).getLifeTimeWorldTracker());
		}
	}

	public static void detachServer()
	{
		attachedServer = false;
		INSTANCE.stop();
	}

	public static boolean isActivated()
	{
		return attachedServer && INSTANCE.isTracking();
	}

	public Stream<String> getAvailableEntityType()
	{
		if (!isActivated())
		{
			return Stream.empty();
		}
		return this.trackers.values().stream().
				flatMap(
						tracker -> tracker.getDataMap().keySet().
						stream().map(LifeTimeTrackerUtil::getEntityTypeDescriptor)
				).
				distinct();
	}

	@Override
	protected void initTracker()
	{
		this.trackers.values().forEach(LifeTimeWorldTracker::initTracker);
	}

	@Override
	protected void printTrackingResult(ServerCommandSource source, boolean realtime)
	{
		try
		{
			long ticks = this.sendTrackedTime(source, realtime);
			int count = this.trackers.values().stream().
					mapToInt(tracker -> tracker.print(source, ticks, null, null)).
					sum();
			if (count == 0)
			{
				Messenger.m(source, Messenger.s(this.tr("no_result", "No result yet")));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	protected int printTrackingResultSpecific(ServerCommandSource source, String entityTypeString, SpecificDetailMode detailMode, boolean realtime)
	{
		Optional<EntityType<?>> entityTypeOptional = Registry.ENTITY_TYPE.stream().
				filter(entityType -> LifeTimeTrackerUtil.getEntityTypeDescriptor(entityType).equals(entityTypeString)).findFirst();
		if (entityTypeOptional.isPresent())
		{
			long ticks = this.sendTrackedTime(source, realtime);
			EntityType<?> entityType = entityTypeOptional.get();
			source.sendFeedback(Messenger.c(
					"w " + this.tr("specific_result.pre", "Life time result for "),
					entityType.getName(),
					"w " + this.tr("specific_result.post", "")
					), false);
			int count = this.trackers.values().stream().
					mapToInt(tracker -> tracker.print(source, ticks, entityType, detailMode)).
					sum();
			if (count == 0)
			{
				Messenger.m(source, Messenger.s(this.tr("no_result", "No result yet")));
			}
		}
		else
		{
			Messenger.m(source, Messenger.s(this.tr("Unknown entity type") + ": " + entityTypeString, "r"));
		}
		return 1;
	}
}
