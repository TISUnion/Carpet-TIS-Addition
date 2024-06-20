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

package carpettisaddition.mixins.network;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.network.TISCMCustomPayload;
import carpettisaddition.network.TISCMServerPacketHandler;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if 12002 <= MC && MC < 12005
//$$ import net.minecraft.server.network.ServerCommonNetworkHandler;
//#endif

@Mixin(
		//#if 12002 <= MC && MC < 12005
		//$$ ServerCommonNetworkHandler.class
		//#else
		ServerPlayNetworkHandler.class
		//#endif
)
public abstract class ServerPlayNetworkHandlerMixin
{
	@Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
	private void onCustomPayload$TISCM(CustomPayloadC2SPacket packet, CallbackInfo ci)
	{
		if (!CarpetTISAdditionSettings.tiscmNetworkProtocol)
		{
			return;
		}

		//#if MC >= 12002
		//$$ if (packet.payload() instanceof TISCMCustomPayload tiscmCustomPayload && (Object)this instanceof ServerPlayNetworkHandler self)
		//$$ {
		//$$ 	TISCMServerPacketHandler.getInstance().dispatch(self, tiscmCustomPayload);
		//$$ 	ci.cancel();
		//$$ }
		//#else
		Identifier channel = ((CustomPayloadC2SPacketAccessor)packet).getChannel();
		if (TISCMCustomPayload.ID.equals(channel))
		{
			TISCMCustomPayload tiscmCustomPayload = new TISCMCustomPayload(((CustomPayloadC2SPacketAccessor)packet).getData());
			TISCMServerPacketHandler.getInstance().dispatch((ServerPlayNetworkHandler)(Object)this, tiscmCustomPayload);
			ci.cancel();
		}
		//#endif
	}
}
