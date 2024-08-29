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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.debug.TickChart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//#if MC >= 12005
//$$ import net.minecraft.util.profiler.MultiValueDebugSampleLogImpl;
//#endif

/**
 * mc1.14 ~ mc1.20.1: subproject 1.15.2 (main project)
 * mc1.20.2+        : subproject 1.20.2        <--------
 */
@Mixin(DebugHud.class)
public abstract class DebugHudMixin
{
	@Shadow @Final private TextRenderer textRenderer;
	@Shadow @Final private MinecraftClient client;

	//#if MC >= 12003
	//$$ @Shadow @Final private TickChart tickChart;
	//#endif

	//#if MC >= 12005
	//$$ @Shadow @Final private MultiValueDebugSampleLogImpl tickNanosLog;
	//#endif

	@Inject(
			//#if MC >= 12102
			//$$ method = "render",
			//#else
			method = "method_51746",  // lambda method in render()
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12005
					//$$ target = "Lnet/minecraft/util/profiler/MultiValueDebugSampleLogImpl;getLength()I"
					//#else
					target = "Lnet/minecraft/client/MinecraftClient;getServer()Lnet/minecraft/server/integrated/IntegratedServer;"
					//#endif
			)
	)
	private void syncServerMsptMetricsData_drawIfPossible(
			DrawContext drawContext, CallbackInfo ci,
			@Local(ordinal = 0) int windowWidth,
			@Local(ordinal = 1) int centerX
	)
	{
		boolean shouldVanillaChartDraw =
				//#if MC >= 12005
				//$$ this.tickNanosLog.getLength() > 0;
				//#else
				this.client.getServer() != null;
				//#endif
		if (!shouldVanillaChartDraw)
		{
			if (ServerMsptMetricsDataSyncer.getInstance().isServerSupportOk())
			{
				var data = ServerMsptMetricsDataSyncer.getInstance().getMetricsData();
				//#if MC >= 12005
				//$$ if (data.getLength() <= 0)
				//$$ {
				//$$ 	return;
				//$$ }
				//#endif

				var chart = new TickChart(
						this.textRenderer, data
						//#if MC >= 12003
						//$$ , ((TickChartAccessor)this.tickChart).getMillisPerTickSupplier()
						//#endif
				);

				// vanilla copy
				int width = chart.getWidth(centerX);
				chart.render(drawContext, windowWidth - width, width);
			}
		}
	}
}
