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
import carpettisaddition.network.TISCMClientPacketHandler;
import carpettisaddition.network.TISCMProtocol;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin
{
	@Inject(
			method = "onCustomPayload",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/packet/s2c/play/CustomPayloadS2CPacket;getChannel()Lnet/minecraft/util/Identifier;",
					ordinal = 0
			),
			cancellable = true
	)
	private void onCustomPayload$TISCM(CustomPayloadS2CPacket packet, CallbackInfo ci)
	{
		Identifier channel = ((CustomPayloadS2CPacketAccessor) packet).getChannel();
		if (TISCMProtocol.CHANNEL.equals(channel))
		{
			TISCMClientPacketHandler.getInstance().dispatch((ClientPlayNetworkHandler) (Object) this, ((CustomPayloadS2CPacketAccessor) packet).getData());
			ci.cancel();
		}
	}

	@Inject(method = "onGameJoin", at = @At("RETURN"))
	private void playerJoinClientHookOnGameJoin$TISCM(CallbackInfo ci)
	{
		TISCMClientPacketHandler.getInstance().onConnectedToNewServer();
	}

	@Inject(method = "onPlayerRespawn", at = @At("RETURN"))
	private void playerJoinClientHookOnPlayerRespawn$TISCM(CallbackInfo ci)
	{
		TISCMClientPacketHandler.getInstance().onConnectedToNewServer();
	}
}
