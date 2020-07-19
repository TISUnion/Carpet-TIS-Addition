package carpettisaddition.helpers;

import carpet.utils.Messenger;
import carpettisaddition.commands.RaidCommand;
import carpettisaddition.logging.logHelpers.RaidLogHelper;
import carpettisaddition.utils.TranslatableBase;
import carpettisaddition.utils.Util;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.Raid;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;
import java.util.Map;


public class RaidTracker extends TranslatableBase
{
	public static RaidTracker inst = new RaidTracker();

	private boolean isTracking;
	private long startTick;
	private int raidGeneratedCount;
	private final Map<EntityType<?>, Integer> raiderCounter = Maps.newHashMap();
	private final Map<RaidLogHelper.InvalidateReason, Integer> raidInvalidateCounter = Maps.newHashMap();

	public RaidTracker()
	{
		super(null, "raid_tracker");
	}

	private int __startTracking(ServerCommandSource source, boolean info)
	{
		isTracking = true;
		startTick = Util.getGameTime();
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
			} else if (info)
			{
				Messenger.m(source, Messenger.s(tr("tracking_not_started", "Raid tracking is not started")));
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

	public void __trackRaidInvalidated(RaidLogHelper.InvalidateReason reason)
	{
		if (isTracking)
		{
			Integer count = raidInvalidateCounter.get(reason);
			raidInvalidateCounter.put(reason, count == null ? 1 : count + 1);
		}
	}
	public static void trackRaidInvalidated(RaidLogHelper.InvalidateReason reason)
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
			Messenger.m(source, Messenger.s(tr("tracking_not_started", "Raid tracking is not started")));
			return 1;
		}
		List<Object> result = Lists.newArrayList();
		long ticks = Math.max(1, Util.getGameTime() - startTick);

		result.add(Messenger.c("bw --------------------\n"));
		result.add(Messenger.c(String.format("w %s %.2f min\n", tr("Tracked"), (double)ticks / (20 * 60))));
		result.add(Messenger.c(String.format("w %s: %s\n", tr("Raid generated"), Util.ratePerHour(raidGeneratedCount, ticks))));
		result.add(Messenger.c(String.format("w %s: %s\n", RaidCommand.inst.tr("Raiders"), Util.ratePerHour(raiderCounter.values().stream().mapToInt(Integer::intValue).sum(), ticks))));
		raiderCounter.forEach((raiderType, count) ->
				result.add(Messenger.c(
						"w - ",
						raiderType.getName(),
						String.format("w : %s", Util.ratePerHour(count, ticks)),
						"w \n"
				))
		);
		result.add(Messenger.c(String.format("w %s: ", tr("Invalidate reasons statistics"))));
		if (raidInvalidateCounter.isEmpty())
		{
			result.add(Messenger.s(tr("None")));
		}
		else
		{
			raidInvalidateCounter.forEach((reason, count) ->
					result.add(Messenger.c(
							String.format("w \n- %s", reason.tr()),
							String.format("w : %s", Util.ratePerHour(count, ticks))
					))
			);
		}

		source.sendFeedback(Messenger.c(result.toArray(new Object[0])), false);
		return 1;
	}
	public static int printTrackingResult(ServerCommandSource source)
	{
		return inst.__printTrackingResult(source);
	}
}
