package carpettisaddition.commands.lifetime;

import carpet.utils.Messenger;
import carpettisaddition.commands.AbstractTracker;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.commands.lifetime.interfaces.ServerWorldWithLifeTimeTracker;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.commands.lifetime.utils.SpecificDetailMode;
import carpettisaddition.utils.TextUtil;
import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.ClickEvent;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class LifeTimeTracker extends AbstractTracker
{
	private static boolean attachedServer = false;
	private static final LifeTimeTracker INSTANCE = new LifeTimeTracker();

	private int currentTrackId = 0;

	private final Map<ServerWorld, LifeTimeWorldTracker> trackers = new Reference2ObjectArrayMap<>();

	public LifeTimeTracker()
	{
		super("LifeTime");
	}

	public static LifeTimeTracker getInstance()
	{
		return INSTANCE;
	}

	public LifeTimeWorldTracker getTracker(World world)
	{
		return world instanceof ServerWorld ? this.trackers.get(world) : null;
	}

	public static void attachServer(MinecraftServer minecraftServer)
	{
		attachedServer = true;
		INSTANCE.trackers.clear();
		for (ServerWorld world : minecraftServer.getWorlds())
		{
			INSTANCE.trackers.put(world, ((ServerWorldWithLifeTimeTracker)world).getLifeTimeWorldTracker());
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

	public boolean willTrackEntity(Entity entity)
	{
		return isActivated() &&
				((LifetimeTrackerTarget)entity).getTrackId() == this.getCurrentTrackId() &&
				LifeTimeTrackerUtil.isTrackedEntity(entity);
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

	public int getCurrentTrackId()
	{
		return this.currentTrackId;
	}

	@Override
	protected void initTracker()
	{
		this.currentTrackId++;
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

	public void sendUnknownEntity(ServerCommandSource source, String entityTypeString)
	{
		Messenger.m(source, Messenger.s(String.format(this.tr("unknown_entity_type", "Unknown entity type \"%s\""), entityTypeString), "r"));
	}

	private void printTrackingResultSpecificInner(ServerCommandSource source, String entityTypeString, String detailModeString, boolean realtime)
	{
		Optional<EntityType<?>> entityTypeOptional = LifeTimeTrackerUtil.getEntityTypeFromName(entityTypeString);
		if (entityTypeOptional.isPresent())
		{
			SpecificDetailMode detailMode = null;
			if (detailModeString != null)
			{
				try
				{
					detailMode = SpecificDetailMode.fromString(detailModeString);
				}
				catch (IllegalArgumentException e)
				{
					Messenger.m(source, Messenger.s(String.format(this.tr("invalid_detail", "Invalid statistic detail \"%s\""), detailModeString), "r"));
					return;
				}
			}

			long ticks = this.sendTrackedTime(source, realtime);
			EntityType<?> entityType = entityTypeOptional.get();
			source.sendFeedback(
					this.advTr("specific_result", "Life time result for %1$s", entityType.getName()),
					false
			);
			SpecificDetailMode finalDetailMode = detailMode;
			int count = this.trackers.values().stream().
					mapToInt(tracker -> tracker.print(source, ticks, entityType, finalDetailMode)).
					sum();
			if (count == 0)
			{
				Messenger.m(source, Messenger.s(this.tr("no_result", "No result yet")));
			}
		}
		else
		{
			this.sendUnknownEntity(source, entityTypeString);
		}
	}

	public int printTrackingResultSpecific(ServerCommandSource source, String entityTypeString, String detailModeString, boolean realtime)
	{
		return this.doWhenTracking(source, () -> this.printTrackingResultSpecificInner(source, entityTypeString, detailModeString, realtime));
	}


	protected int showHelp(ServerCommandSource source)
	{
		String docLink = this.tr("help.doc_link", "https://github.com/TISUnion/Carpet-TIS-Addition#lifetime");
		source.sendFeedback(Messenger.c(
				String.format("wb %s\n", this.getTranslatedNameFull()),
				String.format("w %s\n", this.tr("help.doc_summary", "A tracker to track lifetime and spawn / removal reasons from all newly spawned and removed entities")),
				String.format("w %s", this.tr("help.complete_doc_hint", "Complete doc")),
				TextUtil.getSpaceText(),
				TextUtil.getFancyText(
						null,
						Messenger.s(this.tr("help.here", "here"), "ut"),
						Messenger.s(docLink, "t"),
						new ClickEvent(ClickEvent.Action.OPEN_URL, docLink)
				)
		), false);
		return 1;
	}
}
