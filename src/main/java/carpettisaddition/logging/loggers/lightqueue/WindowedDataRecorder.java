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

package carpettisaddition.logging.loggers.lightqueue;

import carpettisaddition.CarpetTISAdditionSettings;
import com.google.common.collect.Queues;

import java.util.Deque;

public class WindowedDataRecorder
{
	private final Deque<RecordedData> dataQueue = Queues.newArrayDeque();
	private long enqueuedCount = 0;
	private long executedCount = 0;

	public void add(RecordedData newData)
	{
		this.enqueuedCount += newData.enqueuedTask;
		this.executedCount += newData.executedTask;
		this.dataQueue.addLast(newData);
		while (this.dataQueue.size() > CarpetTISAdditionSettings.lightQueueLoggerSamplingDuration)
		{
			RecordedData data = this.dataQueue.removeFirst();
			this.enqueuedCount -= data.enqueuedTask;
			this.executedCount -= data.executedTask;
		}
	}

	public Deque<RecordedData> getQueue()
	{
		return this.dataQueue;
	}

	public long getEnqueuedCount()
	{
		return this.enqueuedCount;
	}

	public long getExecutedCount()
	{
		return this.executedCount;
	}

	public void clear()
	{
		this.dataQueue.clear();
		this.enqueuedCount = 0;
		this.executedCount = 0;
	}
}
