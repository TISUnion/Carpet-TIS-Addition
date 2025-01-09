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

package carpettisaddition.mixins.rule.antiSpamDisabled;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * mc1.14 ~ mc1.21.1: subproject 1.15.2 (main project)        <--------
 * mc1.21.2+        : subproject 1.21.3
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin
{
	@Shadow private int messageCooldown;

	@Shadow private int creativeItemDropThreshold;

	@Inject(
			//#if MC >= 11900
			//$$ method = "checkForSpam",
			//#elseif MC >= 11600
			//$$ method = "onGameMessage",
			//#else
			method = "onChatMessage",
			//#endif
			at = @At("TAIL")
	)
	private void resetMessageCooldown(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.messageCooldown = 0;
		}
	}

	@Inject(method = "onCreativeInventoryAction", at = @At("TAIL"))
	private void resetCreativeItemDropThreshold(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.creativeItemDropThreshold = 0;
		}
	}
}
