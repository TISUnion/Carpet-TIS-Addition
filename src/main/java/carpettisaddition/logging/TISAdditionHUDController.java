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

package carpettisaddition.logging;

import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.logging.loggers.lifetime.LifeTimeHUDLogger;
import carpettisaddition.logging.loggers.lightqueue.LightQueueHUDLogger;
import carpettisaddition.logging.loggers.memory.MemoryHUDLogger;
import carpettisaddition.logging.loggers.scounter.SupplierCounterHUDLogger;
import carpettisaddition.logging.loggers.tickwarp.TickWarpHUDLogger;
import carpettisaddition.logging.loggers.xcounter.XpCounterHUDLogger;
import net.minecraft.server.MinecraftServer;

public class TISAdditionHUDController
{
	public static void updateHUD(MinecraftServer server)
	{
		doHudLogging(TISAdditionLoggerRegistry.__lightQueue, LightQueueHUDLogger.NAME, LightQueueHUDLogger.getInstance());
		doHudLogging(TISAdditionLoggerRegistry.__lifetime, LifeTimeHUDLogger.NAME, LifeTimeHUDLogger.getInstance());
		doHudLogging(TISAdditionLoggerRegistry.__memory, MemoryHUDLogger.NAME, MemoryHUDLogger.getInstance());
		// mobcapsLocal logger has its own injection to make sure it will be updated right after the mobcaps logger
		doHudLogging(TISAdditionLoggerRegistry.__scounter, SupplierCounterHUDLogger.NAME, SupplierCounterHUDLogger.getInstance());
		doHudLogging(TISAdditionLoggerRegistry.__tickWarp, TickWarpHUDLogger.NAME, TickWarpHUDLogger.getInstance());
		doHudLogging(TISAdditionLoggerRegistry.__xcounter, XpCounterHUDLogger.NAME, XpCounterHUDLogger.getInstance());
	}

	public static void doHudLogging(boolean condition, String loggerName, AbstractHUDLogger logger)
	{
		// TODO: move this to AbstractHUDLogger?
		if (condition)
		{
			LoggerRegistry.getLogger(loggerName).log(logger::onHudUpdate);
		}
	}
}
