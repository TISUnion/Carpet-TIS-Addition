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

package carpettisaddition;

import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CarpetTISAdditionClient
{
	private static final CarpetTISAdditionClient INSTANCE = new CarpetTISAdditionClient();
	public static final Logger LOGGER = LogManager.getLogger(CarpetTISAdditionMod.MOD_NAME);

	public static CarpetTISAdditionClient getInstance()
	{
		return INSTANCE;
	}

	public void onClientTick(MinecraftClient client)
	{
		ServerMsptMetricsDataSyncer.getInstance().clientTick();
	}

	public void onClientDisconnected(MinecraftClient client)
	{
		ServerMsptMetricsDataSyncer.getInstance().reset();
	}
}
