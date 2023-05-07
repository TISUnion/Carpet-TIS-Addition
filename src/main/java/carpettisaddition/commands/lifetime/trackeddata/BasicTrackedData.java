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
	public final LifeTimeStatistic lifeTimeStatistic = new LifeTimeStatistic();

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

	public long getSpawningCount()
	{
		return getLongMapSum(this.spawningReasons);
	}

	public long getRemovalCount()
	{
		return this.removalReasons.values().stream().mapToLong(stat -> stat.count).sum();
	}

	/**
	 * Spawn Count: xxxxx
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
	 * Removal Count: xxxxx
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
	 * - AAA: 50, (100/h) 25%
	 * @param reason spawning reason or removal reason
	 */
	private static BaseText getReasonWithRate(AbstractReason reason, long ticks, long count, long total)
	{
		double percentage = 100.0D * count / total;
		return Messenger.c(
				"g - ",
				reason.toText(),
				"g : ",
				CounterUtil.ratePerHourText(count, ticks, "wgg"),
				"w  ",
				Messenger.hover(Messenger.s(String.format("%.1f%%", percentage)), Messenger.s(String.format("%.6f%%", percentage)))
		);
	}

	protected BaseText getSpawningReasonWithRate(SpawningReason reason, long ticks, long count, long total)
	{
		return getReasonWithRate(reason, ticks, count, total);
	}

	protected BaseText getRemovalReasonWithRate(RemovalReason reason, long ticks, long count, long total)
	{
		return getReasonWithRate(reason, ticks, count, total);
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
		List<Map.Entry<SpawningReason, Long>> entryList = Lists.newArrayList(this.spawningReasons.entrySet());
		entryList.sort(Collections.reverseOrder(Comparator.comparingLong(Map.Entry::getValue)));

		// Title for hover mode
		if (!entryList.isEmpty() && hoverMode)
		{
			result.add(Messenger.formatting(tr("reasons_for_spawning"), "e"));
		}

		entryList.forEach(entry -> {
			SpawningReason reason = entry.getKey();
			Long statistic = entry.getValue();

			// added to upper result which will be sent by Messenger.send
			// so each element will be in a separate line
			if (hoverMode)
			{
				result.add(Messenger.s("\n"));
			}

			result.add(this.getSpawningReasonWithRate(reason, ticks, statistic, this.getSpawningCount()));
		});
		return result;
	}

	/**
	 * Reasons for removal
	 * - AAA: 50, (100/h) 25%
	 *   - Minimum lifetime: xx1 gt
	 *   - Maximum lifetime: yy1 gt
	 *   - Average lifetime: zz1 gt
	 * - BBB: 150, (300/h) 75%
	 *   - Minimum lifetime: xx2 gt
	 *   - Maximum lifetime: yy2 gt
	 *   - Average lifetime: zz2 gt
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
			result.add(Messenger.formatting(tr("reasons_for_removal"), "r"));
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
					this.getRemovalReasonWithRate(reason, ticks, statistic.count, this.lifeTimeStatistic.count),
					"w \n",
					statistic.getResult("  ", hoverMode)
			));
		});
		return result;
	}
}
