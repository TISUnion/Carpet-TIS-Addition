package carpettisaddition.helpers;

import carpet.utils.Messenger;
import carpettisaddition.commands.RaidCommand;
import carpettisaddition.logging.loggers.RaidLogger;
import carpettisaddition.translations.TranslatableBase;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.TextUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;

import java.util.List;
import java.util.Map;


public class RaidTracker extends TranslatableBase
{
	public static RaidTracker inst = new RaidTracker();

	private boolean isTracking;
	private long startTick;
	private int raidGeneratedCount;
	private final Map<EntityType<?>, Integer> raiderCounter = Maps.newHashMap();
	private final Map<RaidLogger.InvalidateReason, Integer> raidInvalidateCounter = Maps.newHashMap();

	public RaidTracker()
	{
		super(null, "raid_tracker");
	}

	private int __startTracking(ServerCommandSource source, boolean info)
	{
		if (isTracking)
		{
			Messenger.m(source, Messenger.c(String.format("r %s", tr("tracking_already_started", "Raid tracker has already been running"))));
			return 1;
		}
		isTracking = true;
		startTick = TextUtil.getGameTime();
		raidGeneratedCount = 0;
		raiderCounter.clear();
		raidInvalidateCounter.clear();
		if (info)
		{
			Messenger.m(source, Messenger.s(tr("tracking_started", "Raid tracking started")));
		}
		return 1;
	}
	public static int startTracking(ServerCommandSource source)
	{
		return inst.__startTracking(source, true);
	}

	private int __stopTracking(ServerCommandSource source, boolean info)
	{
		if (source != null)
		{
			if (isTracking)
			{
				printTrackingResult(source);
				if (info)
				{
					Messenger.m(source, Messenger.s(tr("tracking_stopped", "Raid tracking stopped")));
				}
			}
			else if (info)
			{
				Messenger.m(source, Messenger.c(String.format("r %s", tr("tracking_not_started", "Raid tracking is not started"))));
			}
		}
		isTracking = false;
		return 1;
	}
	public static int stopTracking(ServerCommandSource source)
	{
		return inst.__stopTracking(source, true);
	}
	public static void stop()
	{
		stopTracking(null);
	}

	private int __restartTracking(ServerCommandSource source)
	{
		__stopTracking(source, false);
		__startTracking(source, false);
		Messenger.m(source, Messenger.s(tr("tracking_restarted", "Raid tracking restarted")));
		return 1;
	}
	public static int restartTracking(ServerCommandSource source)
	{
		return inst.__restartTracking(source);
	}

	public void __trackRaidInvalidated(RaidLogger.InvalidateReason reason)
	{
		if (isTracking)
		{
			Integer count = raidInvalidateCounter.get(reason);
			raidInvalidateCounter.put(reason, count == null ? 1 : count + 1);
		}
	}
	public static void trackRaidInvalidated(RaidLogger.InvalidateReason reason)
	{
		inst.__trackRaidInvalidated(reason);
	}

	public void __trackRaidGenerated(Raid raid)
	{
		if (isTracking)
		{
			raidGeneratedCount++;
		}
	}
	public static void trackRaidGenerated(Raid raid)
	{
		inst.__trackRaidGenerated(raid);
	}

	public void __trackNewRaider(RaiderEntity raider)
	{
		if (isTracking)
		{
			EntityType<?> key = raider.getType();
			Integer count = raiderCounter.get(key);
			raiderCounter.put(key, count == null ? 1 : count + 1);
		}
	}
	public static void trackNewRaider(RaiderEntity raider)
	{
		inst.__trackNewRaider(raider);
	}

	private int __printTrackingResult(ServerCommandSource source)
	{
		if (!isTracking)
		{
			Messenger.m(source, Messenger.c(String.format("r %s", tr("tracking_not_started", "Raid tracking is not started"))));
			return 1;
		}
		List<BaseText> result = Lists.newArrayList();
		long ticks = Math.max(1, TextUtil.getGameTime() - startTick);
		int raiderCountSum = raiderCounter.values().stream().mapToInt(Integer::intValue).sum();
		int invalidateCounterSum = raidInvalidateCounter.values().stream().mapToInt(Integer::intValue).sum();

		result.add(Messenger.c("bg --------------------"));
		result.add(Messenger.c(String.format("w %s %.2f min", tr("Tracked"), (double)ticks / (20 * 60))));
		result.add(Messenger.c(String.format("w %s: %s", tr("Raid generated"), CounterUtil.ratePerHour(raidGeneratedCount, ticks))));
		result.add(Messenger.c(String.format("w %s: %s", RaidCommand.getInstance().tr("Raiders"), CounterUtil.ratePerHour(raiderCountSum, ticks))));
		raiderCounter.forEach((raiderType, count) -> result.add(Messenger.c(
				"g - ",
				raiderType.getName(),
				String.format("w : %s, %.1f%%", CounterUtil.ratePerHour(count, ticks), (double) count / raiderCountSum * 100))
		));

		result.add(Messenger.c(String.format("w %s: ", tr("Invalidate reasons statistics")), String.format("w %s", raidInvalidateCounter.isEmpty() ? tr("None") : "")));
		raidInvalidateCounter.forEach((reason, count) -> result.add(Messenger.c(
				"g - ",
				String.format("w %s", reason.tr()),
				String.format("w : %s, %.1f%%", CounterUtil.ratePerHour(count, ticks), (double)count / invalidateCounterSum * 100))
		));
		Messenger.send(source, result);
		return 1;
	}
	public static int printTrackingResult(ServerCommandSource source)
	{
		return inst.__printTrackingResult(source);
	}
}
