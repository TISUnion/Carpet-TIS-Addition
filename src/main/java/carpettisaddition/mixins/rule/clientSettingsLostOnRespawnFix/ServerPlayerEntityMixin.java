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

package carpettisaddition.mixins.rule.clientSettingsLostOnRespawnFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
	@Shadow public abstract void setClientSettings(ClientSettingsC2SPacket clientSettingsC2SPacket);

	@Nullable
	private ClientSettingsC2SPacket lastClientSettingsC2SPacket = null;

	@Inject(method = "setClientSettings", at = @At("TAIL"))
	private void storeClientSettingsC2SPacket(ClientSettingsC2SPacket clientSettingsC2SPacket, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.clientSettingsLostOnRespawnFix)
		{
			this.lastClientSettingsC2SPacket = clientSettingsC2SPacket;
		}
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void clientSettingsLostOnRespawnFix(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.clientSettingsLostOnRespawnFix)
		{
			ClientSettingsC2SPacket packet = ((ServerPlayerEntityMixin)(Object)oldPlayer).lastClientSettingsC2SPacket;
			if (packet != null)
			{
				this.setClientSettings(packet);
			}
		}
	}
}
