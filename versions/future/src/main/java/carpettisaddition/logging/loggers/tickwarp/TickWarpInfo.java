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

import carpet.fakes.MinecraftServerInterface;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.mixins.logger.tickwarp.ServerTickRateManagerAccessor;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

// TickWarpInfo impl for mc >= 1.20
public class TickWarpInfo
{
	protected static Optional<ServerTickRateManagerAccessor> trm()
	{
		return Optional.ofNullable(CarpetTISAdditionServer.minecraft_server).
				filter(svr -> svr instanceof MinecraftServerInterface).
				map(svr -> ((MinecraftServerInterface)svr).getTickRateManager()).
				filter(m -> m instanceof ServerTickRateManagerAccessor).
				map(m -> (ServerTickRateManagerAccessor)m);
	}

	public boolean isWarping()
	{
		return trm().map(m -> m.getRemainingWarpTicks() > 0).orElse(false);
	}

	public long getTotalTicks()
	{
		return trm().map(ServerTickRateManagerAccessor::getScheduledCurrentWarpTicks).orElse(0L);
	}

	public long getRemainingTicks()
	{
		return trm().map(ServerTickRateManagerAccessor::getRemainingWarpTicks).orElse(0L);
	}

	public long getStartTime()
	{
		return trm().map(ServerTickRateManagerAccessor::getTickWarpStartTime).orElse(0L);
	}

	public ServerPlayerEntity getTimeAdvancer()
	{
		return trm().map(ServerTickRateManagerAccessor::getWarpResponsiblePlayer).orElse(null);
	}

	public long getCurrentTime()
	{
		return System.nanoTime();
	}
}
