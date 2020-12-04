package carpettisaddition.commands.lifetime.utils;

import carpet.utils.Messenger;
import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.translations.TranslatableBase;
import carpettisaddition.utils.CounterUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A lifetime tracking tracked data per mob type
 */
public class TrackedData extends TranslatableBase
{
	// spawning
	public final Map<SpawningReason, Integer> spawningReasons = Maps.newHashMap();
	public long spawnCount;
	// removal
	public final Map<RemovalReason, LifeTimeStatistic> removalReasons = Maps.newHashMap();
	public final LifeTimeStatistic lifeTimeStatistic = new LifeTimeStatistic();

	public TrackedData()
	{
		super(LifeTimeTracker.getInstance().getTranslator());
	}

	public void updateSpawning(Entity entity, SpawningReason reason)
	{
		this.spawnCount++;
		this.spawningReasons.put(reason, this.spawningReasons.getOrDefault(reason, 0) + 1);
	}

	public void updateRemoval(Entity entity, RemovalReason reason)
	{
		this.lifeTimeStatistic.update(entity);
		this.removalReasons.computeIfAbsent(reason, r -> new LifeTimeStatistic()).update(entity);
	}

	public long getRemovalCount()
	{
		return this.removalReasons.values().stream().mapToLong(stat -> stat.count).sum();
	}

	/**
	 * Spawn Count: xxxxx <divider> Removal Count yyyyy
	 */
	public BaseText getSpawningCountText(long ticks)
	{
		return Messenger.c(
				"q " + this.tr("Spawn Count"),
				"g : ",
				CounterUtil.ratePerHourText(this.spawnCount, ticks, "wgg")
		);
	}

	/**
	 * Removal Count: xxxxx <divider> Removal Count yyyyy
	 */
	public BaseText getRemovalCountText(long ticks)
	{
		return Messenger.c(
				"q " + this.tr("Removal Count"),
				"g : ",
				CounterUtil.ratePerHourText(this.getRemovalCount(), ticks, "wgg")
		);
	}

	// - AAA: 50, (100/h) 25%
	private BaseText getReasonWithRate(AbstractReason reason, long ticks, long count, long total)
	{
		return Messenger.c(
				"g - ",
				reason.toText(),
				"g : ",
				CounterUtil.ratePerHourText(count, ticks, "wgg"),
				String.format("w  %.1f%%", 100.0D * count / total)
		);
	}

	/**
	 * Reasons for spawning
	 * - AAA: 50, (100/h) 25%
	 * - BBB: 150, (300/h) 75%
	 *
	 * @param hoverMode automatically insert a new line text between lines or not for hover text display
	 * @return might be a empty list
	 */
	public List<BaseText> getSpawningReasonsTexts(long ticks, boolean hoverMode)
	{
		List<BaseText> result = Lists.newArrayList();
		List<Map.Entry<SpawningReason, Integer>> entryList = Lists.newArrayList(this.spawningReasons.entrySet());
		entryList.sort(Collections.reverseOrder(Comparator.comparingInt(Map.Entry::getValue)));

		// Title for hover mode
		if (!entryList.isEmpty() && hoverMode)
		{
			result.add(Messenger.s(this.tr("Reasons for spawning"), "e"));
		}

		entryList.forEach(entry -> {
			// added to upper result which will be sent by Messenger.send
			// so each element will be in a separate line
			if (hoverMode)
			{
				result.add(Messenger.s("\n"));
			}

			result.add(this.getReasonWithRate(entry.getKey(), ticks, entry.getValue(), this.spawnCount));
		});
		return result;
	}

	/**
	 * Reasons for removal
	 * - AAA: 50, (100/h) 25%
	 *   - Minimum life time: xx1 gt
	 *   - Maximum life time: yy1 gt
	 *   - Average life time: zz1 gt
	 * - BBB: 150, (300/h) 75%
	 *   - Minimum life time: xx2 gt
	 *   - Maximum life time: yy2 gt
	 *   - Average life time: zz2 gt
	 *
	 * @param hoverMode automatically insert a new line text between lines or not for hover text display
	 * @return might be a empty list
	 */
	public List<BaseText> getRemovalReasonsTexts(long ticks, boolean hoverMode)
	{
		List<BaseText> result = Lists.newArrayList();
		List<Map.Entry<RemovalReason, LifeTimeStatistic>> entryList = Lists.newArrayList(this.removalReasons.entrySet());
		entryList.sort(Collections.reverseOrder(Comparator.comparingLong(a -> a.getValue().count)));

		// Title for hover mode
		if (!entryList.isEmpty() && hoverMode)
		{
			result.add(Messenger.s(this.tr("Reasons for removal"), "r"));
		}

		entryList.forEach(entry -> {
			RemovalReason reason = entry.getKey();
			LifeTimeStatistic statistic = entry.getValue();

			// added to upper result which will be sent by Messenger.send
			// so each element will be in a separate line
			if (hoverMode)
			{
				result.add(Messenger.s("\n"));
			}

			result.add(Messenger.c(
					this.getReasonWithRate(reason, ticks, statistic.count, this.lifeTimeStatistic.count),
					"w \n",
					statistic.getResult("  ")
			));
		});
		return result;
	}
}
