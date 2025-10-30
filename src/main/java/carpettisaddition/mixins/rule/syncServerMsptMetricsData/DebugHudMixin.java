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
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.util.FrameTimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//#if MC >= 12000
//$$ import net.minecraft.client.gui.DrawContext;
//#elseif MC >= 11600
//$$ import net.minecraft.client.util.math.MatrixStack;
//#endif

/**
 * mc1.14 ~ mc1.20.1: subproject 1.15.2 (main project)        <--------
 * mc1.20.2+        : subproject 1.20.2
 */
@Mixin(DebugScreenOverlay.class)
public abstract class DebugHudMixin
{
	@Shadow protected abstract void drawChart(
			//#if MC >= 12000
			//$$ DrawContext ctx,
			//#elseif MC >= 11600
			//$$ MatrixStack matrixStack,
			//#endif
			FrameTimer metricsData, int startY, int firstSample, boolean isClient
	);

	@Inject(
			//#if MC >= 12000
			//$$ // lambda method in method render()
			//$$ method = "method_51746",
			//#else
			method = "render",
			//#endif
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/client/Minecraft;getSingleplayerServer()Lnet/minecraft/client/server/IntegratedServer;",
					shift = At.Shift.AFTER
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void syncServerMsptMetricsData_drawIfPossible(
			//#if MC >= 12000
			//$$ DrawContext arg0,
			//#elseif MC >= 11600
			//$$ MatrixStack arg0,
			//#endif
			CallbackInfo ci,
			//#if MC < 12000
			Entity entity,
			//#endif
			int i, IntegratedServer integratedServer
	)
	{
		if (integratedServer == null)
		{
			if (ServerMsptMetricsDataSyncer.getInstance().isServerSupportOk())
			{
				FrameTimer metricsData = ServerMsptMetricsDataSyncer.getInstance().getMetricsData();

				// vanilla copy
				this.drawChart(
						//#if MC >= 11600
						//$$ arg0,
						//#endif
						metricsData, i - Math.min(i / 2, 240), i / 2, false
				);
			}
		}
	}
}
