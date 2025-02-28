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

import carpettisaddition.commands.lifetime.removal.RemovalReason;
import carpettisaddition.commands.lifetime.spawning.SpawningReason;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerContext;
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.CounterUtils;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.text.BaseText;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class ExtraCountTrackedData extends BasicTrackedData
{
	public final Map<SpawningReason, Long> spawningExtraCountMap = Maps.newHashMap();
	public final Map<RemovalReason, Long> removalExtraCountMap = Maps.newHashMap();

	protected abstract long getExtraCount(Entity entity);

	@Override
	public void updateSpawning(Entity entity, SpawningReason reason)
	{
		super.updateSpawning(entity, reason);
		this.spawningExtraCountMap.put(reason, this.spawningExtraCountMap.getOrDefault(reason, 0L) + this.getExtraCount(entity));
	}

	@Override
	public void updateRemoval(Entity entity, RemovalReason reason)
	{
		super.updateRemoval(entity, reason);
		this.removalExtraCountMap.put(reason, this.removalExtraCountMap.getOrDefault(reason, 0L) + this.getExtraCount(entity));
	}

	protected abstract BaseText getCountDisplayText();

	protected abstract String getCountButtonString();

	/**
	 * $text [I]
	 *        ^
	 *        Item Count: zzz, (rrr/h)
	 *
	 * $text [I]
	 *        ^
	 *        Item Count: zzz, (rrr/h) ppp%
	 */
	private BaseText attachExtraCount(BaseText text, long extraCount, @Nullable Long extraTotal, long ticks)
	{
		BaseText extra = Messenger.c(
				this.getCountDisplayText(),
				"g : ",
				CounterUtils.ratePerHourText(extraCount, ticks, "wgg")
		);
		if (extraTotal != null)
		{
			double percentage = 100.0D * extraCount / extraTotal;
			extra.append(Messenger.c("w  ", Messenger.hover(Messenger.s(String.format("%.1f%%", percentage)), Messenger.s(String.format("%.6f%%", percentage)))));
		}

		// console cannot display hover text, so we append the extra count text to the end of the line
		if (CommandUtils.isConsoleCommandSource(LifeTimeTrackerContext.commandSource.get()))
		{
			text = Messenger.c(text, "y  [", extra, "y ]");
		}
		else  // otherwise make a hover button
		{
			text = Messenger.c(
					text, Messenger.s(" "),
					Messenger.hover(Messenger.s("[" + getCountButtonString() + "]", "y"), extra)
			);
		}
		return text;
	}

	/**
	 * Spawn Count: xxx, (yyy/h) [Item Count: zzz, (rrr/h)]
	 */
	@Override
	public BaseText getSpawningCountText(long ticks)
	{
		return this.attachExtraCount(
				super.getSpawningCountText(ticks),
				getLongMapSum(this.spawningExtraCountMap),
				null,
				ticks
		);
	}

	/**
	 * Removal Count: xxx, (yyy/h) [Item Count: zzz, (rrr/h)]
	 */
	@Override
	public BaseText getRemovalCountText(long ticks)
	{
		return this.attachExtraCount(
				super.getRemovalCountText(ticks),
				getLongMapSum(this.removalExtraCountMap),
				null,
				ticks
		);
	}

	/**
	 * AAA: 50, (100/h) 25% [Item Count: zzz, (rrr/h) ppp%]
	 */
	@Override
	protected BaseText getSpawningReasonWithRate(SpawningReason reason, long ticks, long count, long total, String indent)
	{
		return this.attachExtraCount(
				super.getSpawningReasonWithRate(reason, ticks, count, total, indent),
				this.spawningExtraCountMap.getOrDefault(reason, 0L),
				getLongMapSum(this.spawningExtraCountMap),
				ticks
		);
	}

	/**
	 * BBB: 150, (300/h) 75% [Item Count: zzz, (rrr/h) ppp%]
	 */
	@Override
	protected BaseText getRemovalReasonWithRate(RemovalReason reason, long ticks, long count, long total, String indent)
	{
		return this.attachExtraCount(
				super.getRemovalReasonWithRate(reason, ticks, count, total, indent),
				this.removalExtraCountMap.getOrDefault(reason, 0L),
				getLongMapSum(this.removalExtraCountMap),
				ticks
		);
	}
}
