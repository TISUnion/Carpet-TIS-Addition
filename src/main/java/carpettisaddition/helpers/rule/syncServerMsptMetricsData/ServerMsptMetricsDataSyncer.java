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

package carpettisaddition.helpers.rule.syncServerMsptMetricsData;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMProtocol;
import carpettisaddition.network.TISCMServerPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.MetricsData;

public class ServerMsptMetricsDataSyncer
{
	private static final ServerMsptMetricsDataSyncer INSTANCE = new ServerMsptMetricsDataSyncer();

	private MetricsData metricsData;

	private ServerMsptMetricsDataSyncer()
	{
		this.metricsData = new MetricsData();
	}

	public static ServerMsptMetricsDataSyncer getInstance()
	{
		return INSTANCE;
	}

	public void broadcastSample(long timeStamp, long msThisTick)
	{
		TISCMServerPacketHandler.getInstance().broadcast(TISCMProtocol.S2C.MSPT_METRICS_SAMPLE, nbt -> {
			nbt.putLong("time_stamp", timeStamp);
			nbt.putLong("millisecond", msThisTick);
		});
	}

	public void receiveMetricData(CompoundTag nbt)
	{
		long timeStamp = nbt.getLong("time_stamp");
		long msThisTick = nbt.getLong("millisecond");
		this.metricsData.pushSample(msThisTick);
	}

	public MetricsData getMetricsData()
	{
		return this.metricsData;
	}

	public void reset()
	{
		this.metricsData = new MetricsData();
	}

	public boolean isServerSupportOk()
	{
		return CarpetTISAdditionSettings.syncServerMsptMetricsData && TISCMClientPacketHandler.getInstance().isProtocolEnabled();
	}

	public void clientTick()
	{
		// maybe
	}
}
