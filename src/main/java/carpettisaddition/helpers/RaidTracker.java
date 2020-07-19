package carpettisaddition.helpers;

import carpet.utils.Messenger;
import carpettisaddition.logging.logHelpers.RaidLogHelper;
import com.google.common.collect.Maps;
import net.minecraft.entity.raid.Raid;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Map;


public class RaidTracker
{
	private static boolean isTracking;
	private static int raidGeneratedCount;
	private static Map<RaidLogHelper.InvalidateReason, Integer> raidInvalidateCount = Maps.newHashMap();

	public static int startTracking(ServerCommandSource source, boolean info)
	{
		isTracking = true;
		raidGeneratedCount = 0;
		raidInvalidateCount.clear();
		if (info)
		{
			Messenger.m(source, "w Started raid tracking");
		}
		return 1;
	}
	public static int startTracking(ServerCommandSource source)
	{
		return startTracking(source, true);
	}

	public static int stopTracking(ServerCommandSource source, boolean info)
	{
		printTrackingResult(source);
		isTracking = false;
		if (info)
		{
			Messenger.m(source, "w Stopped raid tracking");
		}
		return 1;
	}
	public static int stopTracking(ServerCommandSource source)
	{
		return stopTracking(source, true);
	}

	public static int restartTracking(ServerCommandSource source)
	{
		stopTracking(source, false);
		startTracking(source, false);
		Messenger.m(source, "w Restarted raid tracking");
		return 1;
	}

	public static int printTrackingResult(ServerCommandSource source)
	{

		return 1;
	}
}
