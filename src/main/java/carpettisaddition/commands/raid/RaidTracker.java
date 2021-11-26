package carpettisaddition.commands.raid;

import carpettisaddition.commands.AbstractTracker;
import carpettisaddition.logging.loggers.raid.RaidLogger;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.village.raid.Raid;

import java.util.List;
import java.util.Map;


public class RaidTracker extends AbstractTracker
{
	public static final RaidTracker INSTANCE = new RaidTracker();

	private int raidGeneratedCount;
	private final Map<EntityType<?>, Integer> raiderCounter = Maps.newLinkedHashMap();
	private final Map<RaidLogger.InvalidateReason, Integer> raidInvalidateCounter = Maps.newLinkedHashMap();

	public RaidTracker()
	{
		super("Raid");
	}

	public static RaidTracker getInstance()
	{
		return INSTANCE;
	}

	@Override
	protected void initTracker()
	{
		this.raidGeneratedCount = 0;
		this.raiderCounter.clear();
		this.raidInvalidateCounter.clear();
	}

	public void trackRaidInvalidated(RaidLogger.InvalidateReason reason)
	{
		if (this.isTracking())
		{
			Integer count = this.raidInvalidateCounter.get(reason);
			this.raidInvalidateCounter.put(reason, count == null ? 1 : count + 1);
		}
	}

	public void trackRaidGenerated(Raid raid)
	{
		if (this.isTracking())
		{
			this.raidGeneratedCount++;
		}
	}

	public void trackNewRaider(RaiderEntity raider)
	{
		if (this.isTracking())
		{
			EntityType<?> key = raider.getType();
			Integer count = this.raiderCounter.get(key);
			this.raiderCounter.put(key, count == null ? 1 : count + 1);
		}
	}

	@Override
	protected void printTrackingResult(ServerCommandSource source, boolean realtime)
	{
		long ticks = this.sendTrackedTime(source, realtime);

		List<BaseText> result = Lists.newArrayList();
		int raiderCountSum = this.raiderCounter.values().stream().mapToInt(Integer::intValue).sum();
		int invalidateCounterSum = this.raidInvalidateCounter.values().stream().mapToInt(Integer::intValue).sum();

		result.add(Messenger.c(tr("raid_generated"), "w : ", Messenger.s(CounterUtil.ratePerHour(this.raidGeneratedCount, ticks))));
		result.add(Messenger.c(RaidCommand.getInstance().getTranslator().tr("raiders"), "w : ", Messenger.s(CounterUtil.ratePerHour(raiderCountSum, ticks))));
		this.raiderCounter.forEach((raiderType, count) -> result.add(Messenger.c(
				"g - ",
				raiderType.getName(),
				String.format("w : %s, %.1f%%", CounterUtil.ratePerHour(count, ticks), (double) count / raiderCountSum * 100))
		));

		result.add(Messenger.c(tr("invalidate_reasons_statistics"), "w : ", this.raidInvalidateCounter.isEmpty() ? tr("none") : Messenger.s("")));
		this.raidInvalidateCounter.forEach((reason, count) -> result.add(Messenger.c(
				"g - ",
				reason.toText(),
				String.format("w : %s, %.1f%%", CounterUtil.ratePerHour(count, ticks), (double)count / invalidateCounterSum * 100))
		));
		Messenger.tell(source, result);
	}
}
