/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * Carpet TIS Addition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Carpet TIS Addition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Carpet TIS Addition.  If not, see <https://www.gnu.org/licenses/>.
 */

package carpettisaddition.commands.lifetime.trackeddata;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.commands.lifetime.utils.AbstractReason;
import carpettisaddition.commands.lifetime.utils.LifeTimeStatistic;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.CounterUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * A lifetime-tracking tracked data per mob type
 */
public class BasicTrackedData extends TranslationContext
{
	public final Map<SpawningReason, Long> spawningReasons = Maps.newHashMap();
	public final Map<RemovalReason, LifeTimeStatistic> removalReasons = Maps.newHashMap();
	public final LifeTimeStatistic lifeTimeStatistic = new LifeTimeStatistic();  // sum of removalReasons.values()

	public BasicTrackedData()
	{
		super(LifeTimeTracker.getInstance().getTranslator());
	}

	public void updateSpawning(Entity entity, SpawningReason reason)
	{
		this.spawningReasons.put(reason, this.spawningReasons.getOrDefault(reason, 0L) + 1);
	}

	public void updateRemoval(Entity entity, RemovalReason reason)
	{
		this.lifeTimeStatistic.update(entity);
		this.removalReasons.computeIfAbsent(reason, r -> new LifeTimeStatistic()).update(entity);
	}

	protected static long getLongMapSum(Map<?, Long> longMap)
	{
		return longMap.values().stream().mapToLong(v -> v).sum();
	}

	public boolean hasSpawning()
	{
		return !this.spawningReasons.isEmpty();
	}

	public boolean hasRemoval()
	{
		return !this.removalReasons.isEmpty();
	}

	public long getSpawningCount()
	{
		return getLongMapSum(this.spawningReasons);
	}

	public long getRemovalCount()
	{
		return this.removalReasons.values().stream().mapToLong(stat -> stat.count).sum();
	}

	/**
	 * Spawn Count: xxx, (yyy/h)
	 */
	public BaseText getSpawningCountText(long ticks)
	{
		return Messenger.c(
				Messenger.formatting(tr("spawn_count"), "q"),
				"g : ",
				CounterUtil.ratePerHourText(this.getSpawningCount(), ticks, "wgg")
		);
	}

	/**
	 * Removal Count: xxx, (yyy/h)
	 */
	public BaseText getRemovalCountText(long ticks)
	{
		return Messenger.c(
				Messenger.formatting(tr("removal_count"), "q "),
				"g : ",
				CounterUtil.ratePerHourText(this.getRemovalCount(), ticks, "wgg")
		);
	}

	/**
	 * AAA: 50, (100/h) 25%
	 *
	 * @param reason spawning reason or removal reason
	 */
	private static BaseText getReasonWithRate(AbstractReason reason, long ticks, long count, long total, String indent)
	{
		double percentage = 100.0D * count / total;
		return Messenger.c(
				Messenger.s(indent, "g"),
				reason.toText(),
				"g : ",
				CounterUtil.ratePerHourText(count, ticks, "wgg"),
				"w  ",
				Messenger.hover(Messenger.s(String.format("%.1f%%", percentage)), Messenger.s(String.format("%.6f%%", percentage)))
		);
	}

	/**
	 * AAA: 50, (100/h) 25%
 	 */
	protected BaseText getSpawningReasonWithRate(SpawningReason reason, long ticks, long count, long total, String indent)
	{
		return getReasonWithRate(reason, ticks, count, total, indent);
	}

	/**
	 * BBB: 150, (300/h) 75%
	 */
	protected BaseText getRemovalReasonWithRate(RemovalReason reason, long ticks, long count, long total, String indent)
	{
		return getReasonWithRate(reason, ticks, count, total, indent);
	}

	/**
	 * Reasons for spawning
	 *   AAA: 50, (100/h) 25%
	 *   BBB: 150, (300/h) 75%
	 *
	 * @return might be an empty list
	 */
	public List<BaseText> getSpawningReasonsLines(long ticks)
	{
		List<BaseText> result = Lists.newArrayList();
		long total = this.getSpawningCount();
		this.spawningReasons.entrySet().stream().
				sorted(Collections.reverseOrder(Comparator.comparingLong(Map.Entry::getValue))).
				forEach(entry -> {
					SpawningReason reason = entry.getKey();
					Long statistic = entry.getValue();

					result.add(this.getSpawningReasonWithRate(reason, ticks, statistic, total, "  "));
				});
		return result;
	}

	/**
	 * Reasons for removal
	 *   AAA: 50, (100/h) 25%
	 *     Minimum lifetime: xxx gt [S] [R]
	 *     Maximum lifetime: xxx gt [S] [R]
	 *     Average lifetime: xxx gt
	 *   BBB: 150, (300/h) 75%
	 *     Minimum lifetime: yyy gt [S] [R]
	 *     Maximum lifetime: yyy gt [S] [R]
	 *     Average lifetime: yyy0 gt
	 *
	 * @return might be an empty list
	 */
	public List<BaseText> getRemovalReasonsLines(long ticks, boolean showButton)
	{
		List<BaseText> result = Lists.newArrayList();
		this.removalReasons.entrySet().stream().
				sorted(Collections.reverseOrder(Comparator.comparingLong(a -> a.getValue().count))).
				forEach(entry -> {
					RemovalReason reason = entry.getKey();
					LifeTimeStatistic statistic = entry.getValue();

					result.add(this.getRemovalReasonWithRate(reason, ticks, statistic.count, this.lifeTimeStatistic.count, "  "));
					result.addAll(statistic.getResult("    ", showButton));
				});
		return result;
	}
}
