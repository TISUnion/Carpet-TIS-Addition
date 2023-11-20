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

package carpettisaddition.commands.speedtest.tester;

import carpettisaddition.commands.speedtest.ProgressTimer;
import carpettisaddition.commands.speedtest.SpeedTestCommand;
import carpettisaddition.commands.speedtest.SpeedTestPacketUtils;
import carpettisaddition.translations.TranslationContext;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class SpeedTesterBase extends TranslationContext implements SpeedTester
{
	protected final int totalSizeMb;
	protected final long totalSize;
	protected final AtomicLong processedSize = new AtomicLong(0);
	protected final AtomicInteger processedCount = new AtomicInteger(0);
	protected final ProgressTimer timer = new ProgressTimer();
	protected volatile boolean abort = false;
	private final AtomicBoolean doneExecuted = new AtomicBoolean(false);

	public SpeedTesterBase(int totalSizeMb)
	{
		super(SpeedTestCommand.getInstance().getTranslator());
		this.totalSizeMb = totalSizeMb;
		this.totalSize = (long)totalSizeMb << 20;
	}

	@Override
	public void start()
	{
		this.timer.start();
	}

	@Override
	public void abort()
	{
		this.abort = true;
		this.onDoneWrapper();
	}

	protected void onPayloadDone()
	{
		int processedCount = this.processedCount.addAndGet(1);
		long processedSize = this.processedSize.addAndGet(SpeedTestPacketUtils.SIZE_PER_PACKET);

		this.onProgress(processedSize, this.totalSize);

		if (processedSize >= this.totalSize || this.abort)
		{
			this.onDoneWrapper();
		}
		else
		{
			this.onContinue(processedCount);
		}
	}

	protected void onProgress(long sentBytes, long totalBytes) {}

	/**
	 * Done by send finished, all being aborted. Invoked once
	 */
	protected void onDone(long timeCostNs) {}

	protected void onContinue(int processedCount) {}

	private void onDoneWrapper()
	{
		if (this.doneExecuted.compareAndSet(false, true))
		{
			this.onDone(this.timer.getTimeElapsedNs());
		}
	}
}

