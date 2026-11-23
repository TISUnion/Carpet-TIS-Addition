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
import carpettisaddition.utils.NbtUtils;
import carpettisaddition.utils.compat.TpsDebugDimensions;
import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.FrameTimer;

import java.util.Locale;
import java.util.Set;

//#if MC >= 12005
//$$ import net.minecraft.util.debugchart.LocalSampleLogger;
//#endif

public class ServerMsptMetricsDataSyncer
{
	private static final ServerMsptMetricsDataSyncer INSTANCE = new ServerMsptMetricsDataSyncer();
	private static final TpsDebugDimensions SERVER_TICK_TYPE_FALLBACK = TpsDebugDimensions.TICK_SERVER_METHOD;
	private static final long M = 1_000_000;

	//#if MC >= 12005
	//$$ private LocalSampleLogger
	//#else
	private FrameTimer
	//#endif
			metricsData;

	private final Set<TpsDebugDimensions> sentTypesThisTick = Sets.newHashSet();
	private long tickCounterThisTick = -1;

	private ServerMsptMetricsDataSyncer()
	{
		this.reset();
	}

	public static ServerMsptMetricsDataSyncer getInstance()
	{
		return INSTANCE;
	}

	public void broadcastSample(long tickCounter, long nanosecond, TpsDebugDimensions serverTickType)
	{
		this.broadcastSample(tickCounter, nanosecond, serverTickType, false);
	}

	public void broadcastSampleLegacy(long tickCounter, long nanosecond)
	{
		this.broadcastSample(tickCounter, nanosecond, SERVER_TICK_TYPE_FALLBACK, true);
	}

	private void broadcastSample(long tickCounter, long nanosecond, TpsDebugDimensions serverTickType, boolean useLegacyEncoding)
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
			nbt.putInt("version", 2);
			// Preserve the historical values expected by older clients -- they divide nanoseconds by 1e6.
			long wireNanosecond = useLegacyEncoding ? nanosecond * M : nanosecond;
			nbt.putLong("millisecond", wireNanosecond / M);
			nbt.putLong("nanosecond", wireNanosecond);
			if (useLegacyEncoding)
			{
				nbt.putLong("duration_ns", nanosecond);
			}
			nbt.putString("type", serverTickType.toString().toLowerCase(Locale.ROOT));
		});
	}

	public void receiveMetricData(CompoundTag nbt)
	{
		long nanosecond = NbtUtils.getLongOrZero(nbt, "millisecond") * M;
		if (nbt.contains("nanosecond"))
		{
			nanosecond = NbtUtils.getLongOrZero(nbt, "nanosecond");
		}

		TpsDebugDimensions type;
		try
		{
			type = TpsDebugDimensions.valueOf(NbtUtils.getStringOrEmpty(nbt, "type").toUpperCase(Locale.ROOT));
		}
		catch (IllegalArgumentException e)
		{
			type = SERVER_TICK_TYPE_FALLBACK;
		}

		//#if MC >= 12005
		//$$ if (type.ordinal() == 0)  // FULL_TICK
		//$$ {
		//$$ 	this.metricsData.logSample(nanosecond);
		//$$ }
		//$$ else
		//$$ {
		//$$ 	this.metricsData.logPartialSample(nanosecond, type.ordinal());
		//$$ }
		//#else
		long durationNs = nbt.contains("duration_ns") ? NbtUtils.getLongOrZero(nbt, "duration_ns") : nanosecond / M;
		this.metricsData.logFrameDuration(durationNs);
		//#endif
	}

	public
	//#if MC >= 12005
	//$$ LocalSampleLogger
	//#else
	FrameTimer
	//#endif
	getMetricsData()
	{
		return this.metricsData;
	}

	public void reset()
	{
		this.metricsData =
				//#if MC >= 12005
				//$$ new LocalSampleLogger
				//#else
				new FrameTimer
				//#endif
				(
						//#if MC >= 12005
						//$$ TpsDebugDimensions.values().length
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
