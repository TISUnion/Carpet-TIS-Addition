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

package carpettisaddition.mixins.rule.yeetPacketErrorKick;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.yeetPacketErrorKick.PacketErrorKickYeeter;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc >= 26.4: subproject 26.4  <--------
 * mc <  26.4: subproject 1.15.2 (main project)
 */
@Mixin(ServerCommonPacketListenerImpl.class)
public abstract class ServerCommonPacketListenerImplMixin
{
	// 26.4-snapshot-4 added a disconnect after logging and reporting packet handling exceptions.
	@WrapWithCondition(
			method = "onPacketError(Lnet/minecraft/network/protocol/Packet;Ljava/lang/Exception;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/network/ServerCommonPacketListenerImpl;disconnect(Lnet/minecraft/network/chat/Component;)V"
			)
	)
	private boolean yeetPacketErrorKick_skipDisconnect(
			ServerCommonPacketListenerImpl listener, Component reason,
			@Local(argsOnly = true) Packet<?> packet,
			@Local(argsOnly = true) Exception exception
	)
	{
		if (CarpetTISAdditionSettings.yeetPacketErrorKick && listener instanceof ServerGamePacketListenerImpl gameListener)
		{
			PacketErrorKickYeeter.report(gameListener.player, packet.type().id().toString(), exception);
			return false;
		}
		return true;
	}
}
