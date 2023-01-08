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

import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.entity.Entity;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.MetricsData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//#if MC >= 11600
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

@Mixin(DebugHud.class)
public abstract class DebugHudMixin
{
	@Shadow protected abstract void drawMetricsData(
			//#if MC >= 11600
			//$$ MatrixStack matrixStack,
			//#endif
			MetricsData metricsData, int startY, int firstSample, boolean isClient
	);

	@Inject(
			method = "render",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/client/MinecraftClient;getServer()Lnet/minecraft/server/integrated/IntegratedServer;",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void syncServerMsptMetricsData_drawIfPossible(
			//#if MC >= 11600
			//$$ MatrixStack matrixStack,
			//#endif
			CallbackInfo ci,
			Entity entity, int i, IntegratedServer integratedServer
	)
	{
		if (integratedServer == null)
		{
			if (ServerMsptMetricsDataSyncer.getInstance().isServerSupportOk())
			{
				MetricsData metricsData = ServerMsptMetricsDataSyncer.getInstance().getMetricsData();

				// vanilla copy
				this.drawMetricsData(
						//#if MC >= 11600
						//$$ matrixStack,
						//#endif
						metricsData, i - Math.min(i / 2, 240), i / 2, false
				);
			}
		}
	}
}
