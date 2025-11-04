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

package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;

public class LifeTimeStatistic extends TranslationContext
{
	public static final String COLOR_MIN_TIME = "q ";
	public static final String COLOR_MAX_TIME = "c ";
	public static final String COLOR_AVG_TIME = "p ";

	public StatisticElement minTimeElement;
	public StatisticElement maxTimeElement;
	public long count;
	public long timeSum;

	public LifeTimeStatistic()
	{
		super(LifeTimeTracker.getInstance().getTranslator());
		this.count = 0;
		this.timeSum = 0;
		this.minTimeElement = new StatisticElement(Integer.MAX_VALUE, null, null, null);
		this.maxTimeElement = new StatisticElement(Integer.MIN_VALUE, null, null, null);
	}

	public boolean isValid()
	{
		return this.count > 0;
	}

	public void update(Entity entity)
	{
		long time = ((LifetimeTrackerTarget)entity).getLifeTime$TISCM();
		this.count++;
		this.timeSum += time;
		StatisticElement element = new StatisticElement(time, DimensionWrapper.of(entity), ((LifetimeTrackerTarget)entity).getSpawningPosition$TISCM(), ((LifetimeTrackerTarget)entity).getRemovalPosition$TISCM());
		if (time < this.minTimeElement.time)
		{
			this.minTimeElement = element;
		}
		if (time > this.maxTimeElement.time)
		{
			this.maxTimeElement = element;
		}
	}

	/**
	 * Minimum lifetime: xx gt [S] [R]
	 * Maximum lifetime: yy gt [S] [R]
	 * Average lifetime: zz gt
	 *
	 * or
	 *
	 * N/A
	 *
	 * @param indentString spaces for indent
	 */
	public List<BaseComponent> getResult(String indentString, boolean showButton)
	{
		List<BaseComponent> result = Lists.newArrayList();
		Consumer<BaseComponent> f = text -> result.add(Messenger.c(Messenger.s(indentString), text));
		if (this.isValid())
		{
			f.accept(this.minTimeElement.getTimeWithPos(tr("minimum_lifetime"), COLOR_MIN_TIME, showButton));
			f.accept(this.maxTimeElement.getTimeWithPos(tr("maximum_lifetime"), COLOR_MAX_TIME, showButton));
			f.accept(Messenger.c(
					tr("average_lifetime"),
					"g : ",
					COLOR_AVG_TIME + String.format("%.4f", (double)this.timeSum / this.count),
					"g  gt"
			));
		}
		else
		{
			f.accept(Messenger.s("N/A", "g"));
		}
		return result;
	}

	/**
	 * 10/20/30
	 * 10/20/30 (gt)
	 */
	public BaseComponent getCompressedResult(boolean showGtSuffix)
	{
		if (!this.isValid())
		{
			return Messenger.s("N/A", "g");
		}
		BaseComponent text = Messenger.c(
				COLOR_MIN_TIME + this.minTimeElement.time,
				"g /",
				COLOR_MAX_TIME + this.maxTimeElement.time,
				"g /",
				COLOR_AVG_TIME + String.format("%.2f", (double)this.timeSum / this.count)
		);
		if (showGtSuffix)
		{
			text.append(Messenger.c("g  (gt)"));
		}
		return text;
	}

	public static class StatisticElement
	{
		private final long time;
		private final DimensionWrapper dimensionType;
		private final Vec3 spawningPos;
		private final Vec3 removalPos;

		private StatisticElement(long time, DimensionWrapper dimensionType, Vec3 spawningPos, Vec3 removalPos)
		{
			this.time = time;
			this.dimensionType = dimensionType;
			this.spawningPos = spawningPos;
			this.removalPos = removalPos;
		}

		/**
		 * [hint]: 123 gt
		 * [hint]: 123 gt [S] [R]
		 */
		private BaseComponent getTimeWithPos(BaseComponent hint, String fmt, boolean showButton)
		{
			BaseComponent text = Messenger.c(
					hint,
					"g : ",
					fmt + this.time,
					"g  gt"
			);
			if (showButton)
			{
				text = Messenger.join(
						Messenger.s(" "),
						text,
						LifetimeTexts.spawningPosButton(this.spawningPos, this.dimensionType),
						LifetimeTexts.removalPosButton(this.removalPos, this.dimensionType)
				);
			}
			return text;
		}
	}
}
