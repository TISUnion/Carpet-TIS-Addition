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

package carpettisaddition.mixins.rule.syncServerMsptMetricsData;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * mc1.14 ~ mc1.20.4: subproject 1.15.2 (main project)        <--------
 * mc1.20.5+        : subproject 1.20.5
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin
{
	@Shadow private int ticks;

	@ModifyArg(
			method = "tick",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12002
					//$$ target = "Lnet/minecraft/server/MinecraftServer;tickTickLog(J)V"
					//#else
					target = "Lnet/minecraft/util/MetricsData;pushSample(J)V"
					//#endif
			)
	)
	private long sendServerTpsMetricsData_broadcastTickServer(long msThisTick)
	{
		if (CarpetTISAdditionSettings.syncServerMsptMetricsData)
		{
			ServerMsptMetricsDataSyncer.getInstance().broadcastSample(this.ticks, msThisTick);
		}
		return msThisTick;
	}
}
