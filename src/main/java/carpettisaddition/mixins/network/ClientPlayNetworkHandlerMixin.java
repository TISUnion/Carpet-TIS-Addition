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
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12002
//$$ import net.minecraft.client.network.ClientCommonNetworkHandler;
//$$ import net.minecraft.client.MinecraftClient;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(
		//#if MC >= 12002
		//$$ ClientCommonNetworkHandler.class
		//#else
		ClientPlayNetworkHandler.class
		//#endif
)
public abstract class ClientPlayNetworkHandlerMixin
{
	@Inject(
			//#if MC >= 12002
			//$$ method = "onCustomPayload(Lnet/minecraft/network/packet/s2c/common/CustomPayloadS2CPacket;)V",
			//#else
			method = "onCustomPayload",
			//#endif
			at = @At("HEAD"),
			cancellable = true
	)
	private void onCustomPayload$TISCM(CustomPayloadS2CPacket packet, CallbackInfo ci)
	{
		//#if MC >= 12002
		//$$ if (packet.payload() instanceof TISCMCustomPayload tiscmCustomPayload && (Object)this instanceof ClientPlayNetworkHandler self)
		//$$ {
		//$$ 	TISCMClientPacketHandler.getInstance().dispatch(self, tiscmCustomPayload);
		//$$ 	ci.cancel();
		//$$ }
		//#else
		Identifier channel = ((CustomPayloadS2CPacketAccessor) packet).getChannel();
		if (TISCMCustomPayload.ID.equals(channel))
		{
			PacketByteBuf packetByteBuf = ((CustomPayloadS2CPacketAccessor)packet).getData();
			try
			{
				TISCMCustomPayload tiscmCustomPayload = new TISCMCustomPayload(packetByteBuf);
				TISCMClientPacketHandler.getInstance().dispatch((ClientPlayNetworkHandler)(Object)this, tiscmCustomPayload);
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
