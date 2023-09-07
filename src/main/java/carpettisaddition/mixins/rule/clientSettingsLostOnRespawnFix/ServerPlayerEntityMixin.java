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
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12002
//$$ import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
//#else
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
//#endif

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
	@Shadow public abstract void
			//#if MC >= 12002
			//$$ setClientOptions(SyncedClientOptions settings);
			//#else
			setClientSettings(ClientSettingsC2SPacket settings);
			//#endif

	@Nullable
	//#if MC >= 12002
	//$$ private SyncedClientOptions lastClientSettings$TISCM = null;
	//#else
	private ClientSettingsC2SPacket lastClientSettings$TISCM = null;
	//#endif

	@Inject(
			//#if MC >= 12002
			//$$ method = "setClientOptions",
			//#else
			method = "setClientSettings",
			//#endif
			at = @At("TAIL")
	)
	private void storeClientSettingsC2SPacket(
			//#if MC >= 12002
			//$$ SyncedClientOptions settings,
			//#else
			ClientSettingsC2SPacket settings,
			//#endif
			CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.clientSettingsLostOnRespawnFix)
		{
			this.lastClientSettings$TISCM = settings;
		}
	}

	@Inject(method = "copyFrom", at = @At("TAIL"))
	private void clientSettingsLostOnRespawnFix(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.clientSettingsLostOnRespawnFix)
		{
			//#if MC >= 12002
			//$$ var
			//#else
			ClientSettingsC2SPacket
			//#endif
					settings = ((ServerPlayerEntityMixin)(Object)oldPlayer).lastClientSettings$TISCM;
			if (settings != null)
			{
				//#if MC >= 12002
				//$$ this.setClientOptions(settings);
				//#else
				this.setClientSettings(settings);
				//#endif
			}
		}
	}
}
