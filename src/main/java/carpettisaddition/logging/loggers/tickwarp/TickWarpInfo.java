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

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

//#if MC >= 12000
//$$ import carpet.fakes.MinecraftServerInterface;
//$$ import carpet.helpers.TickRateManager;
//$$ import carpettisaddition.CarpetTISAdditionServer;
//#else
import carpet.helpers.TickSpeed;
//#endif

//#if MC < 12000
@SuppressWarnings("AccessStaticViaInstance")
//#endif
public class TickWarpInfo
{
	//#if MC >= 12000
	//$$ protected static Optional<TickRateManager> trm()
	//$$ {
	//$$ 	return Optional.ofNullable(CarpetTISAdditionServer.minecraft_server).
	//$$ 			filter(svr -> svr instanceof MinecraftServerInterface).
	//$$ 			map(svr -> ((MinecraftServerInterface)svr).getTickRateManager());
	//$$ }
	//#else
	@SuppressWarnings("InstantiationOfUtilityClass")
	protected static Optional<TickSpeed> trm()
	{
		return Optional.of(new TickSpeed());
	}
	//#endif

	public boolean isWarping()
	{
		return trm().map(m -> m.time_bias > 0).orElse(false);
	}

	public long getTotalTicks()
	{
		return trm().map(m -> m.time_warp_scheduled_ticks).orElse(0L);
	}

	public long getRemainingTicks()
	{
		return trm().map(m -> m.time_bias).orElse(0L);
	}

	public long getStartTime()
	{
		return trm().map(m -> m.time_warp_start_time).orElse(0L);
	}

	public ServerPlayerEntity getTimeAdvancer()
	{
		//#if MC >= 11500
		return trm().map(m -> m.time_advancerer).orElse(null);
		//#else
		//$$ return trm().filter(m -> m.time_advancerer instanceof ServerPlayerEntity).map(m -> (ServerPlayerEntity)m.time_advancerer).orElse(null);
		//#endif
	}

	public long getCurrentTime()
	{
		return System.nanoTime();
	}
}
