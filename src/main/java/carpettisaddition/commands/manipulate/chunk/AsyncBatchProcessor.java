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

package carpettisaddition.commands.manipulate.chunk;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class AsyncBatchProcessor<Data>
{
	private final Queue<Data> dataQueue;
	private final Function<Data, CompletableFuture<Void>> dataProcessor;
	private final int maxConcurrency;
	private final int refillThreshold;

	private int activeTaskCount = 0;
	private final CompletableFuture<Void> allDoneFuture = new CompletableFuture<>();
	private final Object lock = new Object();

	public AsyncBatchProcessor(Collection<Data> dataList, Function<Data, CompletableFuture<Void>> dataProcessor, int maxConcurrency, int refillThreshold)
	{
		this.dataQueue = new ArrayDeque<>(dataList);
		this.dataProcessor = dataProcessor;
		this.maxConcurrency = maxConcurrency;
		this.refillThreshold = refillThreshold;
	}

	public AsyncBatchProcessor(Collection<Data> dataList, Function<Data, CompletableFuture<Void>> dataProcessor, int maxConcurrency)
	{
		this(dataList, dataProcessor, maxConcurrency, maxConcurrency - 1);
	}

	public CompletableFuture<Void> start()
	{
		scheduleNext();
		return this.allDoneFuture;
	}

	private void scheduleNext()
	{
		synchronized (this.lock)
		{
			this.scheduleNextNoLock();
		}
	}

	private void scheduleNextNoLock()
	{
		if (this.dataQueue.isEmpty() && this.activeTaskCount == 0)
		{
			this.allDoneFuture.complete(null);
			return;
		}

		if (this.dataQueue.isEmpty() || this.activeTaskCount > this.refillThreshold)
		{
			return;
		}

		while (this.activeTaskCount < this.maxConcurrency && !this.dataQueue.isEmpty())
		{
			Data data = this.dataQueue.poll();
			this.dataProcessor.apply(data).whenComplete((res, ex) -> this.onTaskDone(data));
			this.activeTaskCount++;
		}
	}

	private void onTaskDone(Data data)
	{
		synchronized (this.lock)
		{
			this.activeTaskCount--;
			this.scheduleNextNoLock();
		}
	}
}