/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.syncServerMsptMetricsData;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.profiler.ServerTickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * mc1.14 ~ mc1.20.4: subproject 1.15.2 (main project)
 * mc1.20.5+        : subproject 1.20.5        <--------
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Shadow private int ticks;

	@ModifyArg(
			method = "pushTickLog",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/profiler/log/DebugSampleLog;push(JI)V"
			)
	)
	private long sendServerTpsMetricsData_broadcastTickServer(long milli)
	{
		if (CarpetTISAdditionSettings.syncServerMsptMetricsData)
		{
			ServerMsptMetricsDataSyncer.getInstance().broadcastSample(this.ticks, milli, ServerTickType.TICK_SERVER_METHOD);
		}
		return milli;
	}

	@ModifyArg(
			method = "pushFullTickLog",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/profiler/log/DebugSampleLog;push(J)V"
			)
	)
	private long sendServerTpsMetricsData_broadcastFullTick(long milli)
	{
		if (CarpetTISAdditionSettings.syncServerMsptMetricsData)
		{
			ServerMsptMetricsDataSyncer.getInstance().broadcastSample(this.ticks, milli, ServerTickType.FULL_TICK);
		}
		return milli;
	}

	@ModifyArg(
			method = "pushPerformanceLogs",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/profiler/log/DebugSampleLog;push(JI)V",
					ordinal = 0
			)
	)
	private long sendServerTpsMetricsData_broadcastScheduledTask(long milli)
	{
		if (CarpetTISAdditionSettings.syncServerMsptMetricsData)
		{
			ServerMsptMetricsDataSyncer.getInstance().broadcastSample(this.ticks, milli, ServerTickType.SCHEDULED_TASKS);
		}
		return milli;
	}

	@ModifyArg(
			method = "pushPerformanceLogs",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/profiler/log/DebugSampleLog;push(JI)V",
					ordinal = 1
			)
	)
	private long sendServerTpsMetricsData_broadcastIdle(long milli)
	{
		if (CarpetTISAdditionSettings.syncServerMsptMetricsData)
		{
			ServerMsptMetricsDataSyncer.getInstance().broadcastSample(this.ticks, milli, ServerTickType.IDLE);
		}
		return milli;
	}
}
