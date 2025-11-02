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

import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMCustomPayload;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12002
//$$ import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
//$$ import net.minecraft.client.Minecraft;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(
		//#if MC >= 12002
		//$$ ClientCommonPacketListenerImpl.class
		//#else
		ClientPacketListener.class
		//#endif
)
public abstract class ClientPlayNetworkHandlerMixin
{
	@Inject(
			//#if MC >= 12002
			//$$ method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
			//#else
			method = "handleCustomPayload",
			//#endif
			at = @At("HEAD"),
			cancellable = true
	)
	private void onCustomPayload$TISCM(ClientboundCustomPayloadPacket packet, CallbackInfo ci)
	{
		//#if MC >= 12002
		//$$ if (packet.payload() instanceof TISCMCustomPayload tiscmCustomPayload && (Object)this instanceof ClientPacketListener self)
		//$$ {
		//$$ 	TISCMClientPacketHandler.getInstance().dispatch(self, tiscmCustomPayload);
		//$$ 	ci.cancel();
		//$$ }
		//#else
		ResourceLocation channel = ((CustomPayloadS2CPacketAccessor) packet).getChannel();
		if (TISCMCustomPayload.ID.equals(channel))
		{
			FriendlyByteBuf packetByteBuf = ((CustomPayloadS2CPacketAccessor)packet).getData();
			try
			{
				TISCMCustomPayload tiscmCustomPayload = new TISCMCustomPayload(packetByteBuf);
				TISCMClientPacketHandler.getInstance().dispatch((ClientPacketListener)(Object)this, tiscmCustomPayload);
				ci.cancel();
			}
			finally
			{
				// Fix https://bugs.mojang.com/browse/MC-121884, for TISCM packets
				packetByteBuf.release();
			}
		}
		//#endif
	}
}
