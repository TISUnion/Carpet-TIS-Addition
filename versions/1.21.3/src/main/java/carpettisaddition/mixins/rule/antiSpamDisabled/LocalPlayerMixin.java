/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.antiSpamDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc1.14 ~ mc1.21.1: subproject 1.15.2 (main project, dummy)
 * mc1.21.2+        : subproject 1.21.3        <--------
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin
{
	@ModifyExpressionValue(
			method = "canDropItems",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/TickThrottler;isUnderThreshold()Z"
			)
	)
	private boolean antiSpamDisabled_bypassDropSpamThreshold(boolean original)
	{
		return original || CarpetTISAdditionSettings.antiSpamDisabled;
	}
}
