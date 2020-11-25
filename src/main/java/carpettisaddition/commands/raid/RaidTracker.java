package carpettisaddition.commands.raid;

import carpet.utils.Messenger;
import carpettisaddition.logging.loggers.raid.RaidLogger;
import carpettisaddition.translations.TranslatableBase;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.GameUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.village.raid.Raid;

import java.util.List;
import java.util.Map;


public class RaidTracker extends TranslatableBase
{
	public static final RaidTracker INSTANCE = new RaidTracker();

	private boolean isTracking;
	private long startTick;
	private long startMillis;
	private int raidGeneratedCount;
	private final Map<EntityType<?>, Integer> raiderCounter = Maps.newLinkedHashMap();
	private final Map<RaidLogger.InvalidateReason, Integer> raidInvalidateCounter = Maps.newLinkedHashMap();

	public RaidTracker()
	{
		super(null, "raid_tracker");
	}

	public static RaidTracker getInstance()
	{
		return INSTANCE;
	}

	public int startTracking(ServerCommandSource source, boolean showFeedback)
	{
		if (this.isTracking)
		{
			if (showFeedback)
			{
				Messenger.m(source, Messenger.c(String.format("r %s", tr("tracking_already_started", "Raid tracker is already running"))));
			}
			return 1;
		}
		this.isTracking = true;
		this.startTick = GameUtil.getGameTime();
		this.startMillis = System.currentTimeMillis();
		this.raidGeneratedCount = 0;
		this.raiderCounter.clear();
		this.raidInvalidateCounter.clear();
		if (showFeedback)
		{
			Messenger.m(source, Messenger.s(tr("tracking_started", "Raid tracking started")));
		}
		return 1;
	}

	public int stopTracking(ServerCommandSource source, boolean showFeedback)
	{
		if (source != null)
		{
			if (this.isTracking)
			{
				this.printTrackingResult(source, false);
				if (showFeedback)
				{
					Messenger.m(source, Messenger.s(tr("tracking_stopped", "Raid tracking stopped")));
				}
			}
			else if (showFeedback)
			{
				Messenger.m(source, Messenger.c(String.format("r %s", tr("tracking_not_started", "Raid tracking has not started"))));
			}
		}
		this.isTracking = false;
		return 1;
	}

	// call this when server stops
	public void stop()
	{
		this.stopTracking(null, false);
	}

	public int restartTracking(ServerCommandSource source)
	{
		this.stopTracking(source, false);
		this.startTracking(source, false);
		Messenger.m(source, Messenger.s(tr("tracking_restarted", "Raid tracking restarted")));
		return 1;
	}

	public void trackRaidInvalidated(RaidLogger.InvalidateReason reason)
	{
		if (this.isTracking)
		{
			Integer count = this.raidInvalidateCounter.get(reason);
			this.raidInvalidateCounter.put(reason, count == null ? 1 : count + 1);
		}
	}

	public void trackRaidGenerated(Raid raid)
	{
		if (this.isTracking)
		{
			this.raidGeneratedCount++;
		}
	}

	public void trackNewRaider(RaiderEntity raider)
	{
		if (this.isTracking)
		{
			EntityType<?> key = raider.getType();
			Integer count = this.raiderCounter.get(key);
			this.raiderCounter.put(key, count == null ? 1 : count + 1);
		}
	}

	public int printTrackingResult(ServerCommandSource source, boolean realtime)
	{
		if (!this.isTracking)
		{
			Messenger.m(source, Messenger.c(String.format("r %s", tr("tracking_not_started", "Raid tracking has not started"))));
			return 1;
		}
		List<BaseText> result = Lists.newArrayList();
		long ticks = Math.max(1, realtime ? (System.currentTimeMillis() - this.startMillis) / 50 : GameUtil.getGameTime() - this.startTick);
		int raiderCountSum = this.raiderCounter.values().stream().mapToInt(Integer::intValue).sum();
		int invalidateCounterSum = this.raidInvalidateCounter.values().stream().mapToInt(Integer::intValue).sum();

		result.add(Messenger.c("bg --------------------"));
		result.add(Messenger.c(String.format("w %s %.2f min (%s)", tr("Tracked"), (double)ticks / (20 * 60), realtime ? tr("real time") : tr("in game"))));
		result.add(Messenger.c(String.format("w %s: %s", tr("Raid generated"), CounterUtil.ratePerHour(this.raidGeneratedCount, ticks))));
		result.add(Messenger.c(String.format("w %s: %s", RaidCommand.getInstance().tr("Raiders"), CounterUtil.ratePerHour(raiderCountSum, ticks))));
		this.raiderCounter.forEach((raiderType, count) -> result.add(Messenger.c(
				"g - ",
				raiderType.getName(),
				String.format("w : %s, %.1f%%", CounterUtil.ratePerHour(count, ticks), (double) count / raiderCountSum * 100))
		));

		result.add(Messenger.c(String.format("w %s: ", tr("Invalidate reasons statistics")), String.format("w %s", this.raidInvalidateCounter.isEmpty() ? tr("None") : "")));
		this.raidInvalidateCounter.forEach((reason, count) -> result.add(Messenger.c(
				"g - ",
				String.format("w %s", reason.tr()),
				String.format("w : %s, %.1f%%", CounterUtil.ratePerHour(count, ticks), (double)count / invalidateCounterSum * 100))
		));
		Messenger.send(source, result);
		return 1;
	}
}
