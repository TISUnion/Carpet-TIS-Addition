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
import carpettisaddition.utils.compat.ServerTickType;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.MetricsData;

import java.util.Set;

public class ServerMsptMetricsDataSyncer
{
	private static final ServerMsptMetricsDataSyncer INSTANCE = new ServerMsptMetricsDataSyncer();
	private static final ServerTickType SERVER_TICK_TYPE_FALLBACK = ServerTickType.TICK_SERVER_METHOD;

	private MetricsData metricsData;
	private final Set<ServerTickType> sentTypesThisTick = Sets.newHashSet();
	private long tickCounterThisTick = -1;

	private ServerMsptMetricsDataSyncer()
	{
		this.reset();
	}

	public static ServerMsptMetricsDataSyncer getInstance()
	{
		return INSTANCE;
	}

	public void broadcastSample(long tickCounter, long millisecond, ServerTickType serverTickType)
	{
		if (tickCounter != this.tickCounterThisTick)
		{
			this.sentTypesThisTick.clear();
			this.tickCounterThisTick = tickCounter;
		}
		if (!this.sentTypesThisTick.add(serverTickType))
		{
			return;
		}

		TISCMServerPacketHandler.getInstance().broadcast(TISCMProtocol.S2C.MSPT_METRICS_SAMPLE, nbt -> {
			nbt.putLong("millisecond", millisecond);
			nbt.putString("type", serverTickType.toString().toLowerCase());
		});
	}

	public void broadcastSample(long tickCounter, long millisecond)
	{
		broadcastSample(tickCounter, millisecond, SERVER_TICK_TYPE_FALLBACK);
	}

	public void receiveMetricData(CompoundTag nbt)
	{
		long millisecond = nbt.getLong("millisecond");

		ServerTickType type;
		try
		{
			type = ServerTickType.valueOf(nbt.getString("type").toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			type = SERVER_TICK_TYPE_FALLBACK;
		}

		//#if MC >= 12005
		//$$ if (type.ordinal() == 0)  // FULL_TICK
		//$$ {
		//$$ 	this.metricsData.push(millisecond);
		//$$ }
		//$$ else
		//$$ {
		//$$ 	this.metricsData.push(millisecond, type.ordinal());
		//$$ }
		//#else
		this.metricsData.pushSample(millisecond);
		//#endif
	}

	public MetricsData getMetricsData()
	{
		return this.metricsData;
	}

	public void reset()
	{
		this.metricsData = new MetricsData(
				//#if MC >= 12005
				//$$ net.minecraft.util.profiler.ServerTickType.values().length
				//#endif
		);
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
