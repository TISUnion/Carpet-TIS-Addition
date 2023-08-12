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
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.class_8759;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin
{
	@Shadow @Final
	private TextRenderer textRenderer;

	@ModifyReturnValue(method = "method_53467", at = @At(value = "RETURN", ordinal = 1))
	private class_8759 syncServerMsptMetricsData_drawIfPossible(class_8759 ret)
	{
		if (ret == null)
		{
			if (ServerMsptMetricsDataSyncer.getInstance().isServerSupportOk())
			{
				var data = ServerMsptMetricsDataSyncer.getInstance().getMetricsData();
				ret = new class_8759(this.textRenderer, data);
			}
		}
		return ret;
	}
}
