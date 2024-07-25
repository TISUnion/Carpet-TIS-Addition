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

package carpettisaddition.logging.loggers.tickwarp;

import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

//#if MC < 12003
import carpet.helpers.TickSpeed;
//#endif

public class MemorizedTickWarpInfo implements TickWarpInfo
{
	private final TickWarpInfo delegate;
	private long totalTicks;
	private long timeRemaining;
	private long elapsedTime;
	private boolean recordedSomething = false;
	private long lastRecordingTime;
	private ServerCommandSource lastTimeAdvancer;

	public MemorizedTickWarpInfo(TickWarpInfo delegate)
	{
		this.delegate = delegate;
	}

	/**
	 * Should be called at the end of {@link TickSpeed#tickrate_advance}
	 * Carpet mod might reset the advancer field before {@link TickSpeed#finish_time_warp} so we record it at the beginning
	 */
	@Override
	public void setTimeAdvancer(@Nullable ServerCommandSource timeAdvancer)
	{
		this.delegate.setTimeAdvancer(timeAdvancer);
	}

	/**
	 * Only record if the tick warping has started, so /tick warp 0 won't mess the result
	 * Should be called at the beginning of {@link TickSpeed#finish_time_warp}
	 */
	public void recordResultIfsuitable()
	{
		if (this.delegate.getTotalTicks() != 0)
		{
			this.totalTicks = this.delegate.getTotalTicks();
			this.timeRemaining = this.delegate.getRemainingTicks();
			this.elapsedTime = this.delegate.getElapsedTime();
			this.lastTimeAdvancer = this.delegate.getTimeAdvancer();
			this.recordedSomething = true;
			this.lastRecordingTime = System.nanoTime();
		}
	}

	@Override
	public boolean isWarping()
	{
		return this.delegate.isWarping();
	}

	@Override
	public long getTotalTicks()
	{
		return this.totalTicks;
	}

	@Override
	public long getRemainingTicks()
	{
		return this.timeRemaining;
	}

	@Override
	public long getElapsedTime()
	{
		return this.elapsedTime;
	}

	@Override
	@Nullable
	public ServerCommandSource getTimeAdvancer()
	{
		return this.lastTimeAdvancer;
	}

	public boolean hasData()
	{
		return this.recordedSomething;
	}

	public long getLastRecordingTime()
	{
		return this.lastRecordingTime;
	}
}
