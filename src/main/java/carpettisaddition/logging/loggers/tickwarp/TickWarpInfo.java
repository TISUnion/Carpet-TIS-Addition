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
import net.minecraft.server.network.ServerPlayerEntity;

public class TickWarpInfo
{
	public boolean isWarping()
	{
		return TickSpeed.time_bias > 0;
	}

	public long getTotalTicks()
	{
		return TickSpeed.time_warp_scheduled_ticks;
	}

	public long getRemainingTicks()
	{
		return TickSpeed.time_bias;
	}

	public long getStartTime()
	{
		return TickSpeed.time_warp_start_time;
	}

	public ServerPlayerEntity getTimeAdvancer()
	{
		//#if MC >= 11500
		return TickSpeed.time_advancerer;
		//#else
		//$$ return TickSpeed.time_advancerer instanceof ServerPlayerEntity ? (ServerPlayerEntity)TickSpeed.time_advancerer : null;
		//#endif
	}

	public long getCurrentTime()
	{
		return System.nanoTime();
	}
}
