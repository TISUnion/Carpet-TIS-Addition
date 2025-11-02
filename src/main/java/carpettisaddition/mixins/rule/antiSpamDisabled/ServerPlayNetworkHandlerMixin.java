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
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * mc1.14 ~ mc1.21.1: subproject 1.15.2 (main project)        <--------
 * mc1.21.2+        : subproject 1.21.3
 */
@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPlayNetworkHandlerMixin
{
	@Shadow private int chatSpamTickCount;

	@Shadow private int dropSpamTickCount;

	@Inject(
			//#if MC >= 11900
			//$$ method = "detectRateSpam",
			//#elseif MC >= 11700
			//$$ method = "handleChat(Lnet/minecraft/server/network/TextFilter$FilteredText;)V",
			//#elseif MC >= 11600
			//$$ method = "handleChat(Ljava/lang/String;)V",
			//#else
			method = "handleChat",
			//#endif
			at = @At("TAIL")
	)
	private void resetMessageCooldown(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.chatSpamTickCount = 0;
		}
	}

	@Inject(method = "handleSetCreativeModeSlot", at = @At("TAIL"))
	private void resetCreativeItemDropThreshold(CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.antiSpamDisabled)
		{
			this.dropSpamTickCount = 0;
		}
	}
}
