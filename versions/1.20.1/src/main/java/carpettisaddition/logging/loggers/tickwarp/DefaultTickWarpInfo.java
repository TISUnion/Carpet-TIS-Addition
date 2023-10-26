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

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.mixins.logger.tickwarp.ServerTickRateManagerAccessor;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//#if MC >= 12003
//$$ import net.minecraft.server.MinecraftServer;
//#else
import carpet.fakes.MinecraftServerInterface;
//#endif

/**
 * The TickWarpInfo impl that extracts information from carpet mod (mc >= 1.20 version-specific impl)
 */
public class DefaultTickWarpInfo implements TickWarpInfo
{
	private ServerCommandSource timeAdvancer;

	@Override
	@Nullable
	public ServerCommandSource getTimeAdvancer()
	{
		return this.timeAdvancer;
	}

	@Override
	public void setTimeAdvancer(@Nullable ServerCommandSource timeAdvancer)
	{
		this.timeAdvancer = timeAdvancer;
	}

	protected static Optional<ServerTickRateManagerAccessor> trm()
	{
		return Optional.ofNullable(CarpetTISAdditionServer.minecraft_server).
				//#if MC >= 12003
				//$$ map(MinecraftServer::method_54833).
				//#else
				filter(svr -> svr instanceof MinecraftServerInterface).
				map(svr -> ((MinecraftServerInterface)svr).getTickRateManager()).
				//#endif
				filter(m -> m instanceof ServerTickRateManagerAccessor).
				map(m -> (ServerTickRateManagerAccessor)m);
	}

	@Override
	public boolean isWarping()
	{
		return trm().map(m -> m.getRemainingWarpTicks() > 0).orElse(false);
	}

	@Override
	public long getTotalTicks()
	{
		return trm().map(ServerTickRateManagerAccessor::getScheduledCurrentWarpTicks).orElse(0L);
	}

	@Override
	public long getRemainingTicks()
	{
		return trm().map(ServerTickRateManagerAccessor::getRemainingWarpTicks).orElse(0L);
	}

	@Override
	public long getStartTime()
	{
		return trm().map(ServerTickRateManagerAccessor::getTickWarpStartTime).orElse(0L);
	}

	@Override
	public long getCurrentTime()
	{
		return System.nanoTime();
	}
}
