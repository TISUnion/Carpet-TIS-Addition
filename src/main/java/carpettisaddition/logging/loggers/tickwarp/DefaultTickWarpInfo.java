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

import carpet.helpers.TickSpeed;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

/**
 * The TickWarpInfo impl that extracts information from carpet mod (mc < 1.20 version-specific impl)
 */
public class DefaultTickWarpInfo implements TickWarpInfo
{
	private CommandSourceStack timeAdvancer;

	@Override
	@Nullable
	public CommandSourceStack getTimeAdvancer()
	{
		return this.timeAdvancer;
	}

	@Override
	public void setTimeAdvancer(@Nullable CommandSourceStack timeAdvancer)
	{
		this.timeAdvancer = timeAdvancer;
	}

	@Override
	public boolean isWarping()
	{
		return TickSpeed.time_bias > 0;
	}

	@Override
	public long getTotalTicks()
	{
		return TickSpeed.time_warp_scheduled_ticks;
	}

	@Override
	public long getRemainingTicks()
	{
		return TickSpeed.time_bias;
	}

	@Override
	public long getElapsedTime()
	{
		return System.nanoTime() - TickSpeed.time_warp_start_time;
	}
}
