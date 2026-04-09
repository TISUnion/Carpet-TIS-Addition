/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.logging.loggers.entityIdCounter;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.OptionalDouble;

public class EntityIdCounterSampler
{
	private final Deque<Long> window = Queues.newArrayDeque();
	private long windowSum;

	private Integer lastValue = null;

	public void recordForOneTick(int value)
	{
		long thisDelta = 0;
		if (this.lastValue != null)
		{
			if (value >= this.lastValue)
			{
				thisDelta = value - this.lastValue;
			}
			else
			{
				thisDelta = ((long)value - (long)Integer.MIN_VALUE) + ((long)Integer.MAX_VALUE - this.lastValue) + 1;
			}
		}
		this.lastValue = value;

		this.addToWindow(thisDelta);
	}

	private void addToWindow(long item)
	{
		this.windowSum += item;
		this.window.addLast(item);

		if (this.window.size() > CarpetTISAdditionSettings.entityIdCounterLoggerSamplingDuration)
		{
			this.windowSum -= this.window.removeFirst();
		}
	}

	public OptionalDouble getRatePerGt()
	{
		return this.window.isEmpty() ? OptionalDouble.empty() : OptionalDouble.of(1.0 * this.windowSum / this.window.size());
	}
}
