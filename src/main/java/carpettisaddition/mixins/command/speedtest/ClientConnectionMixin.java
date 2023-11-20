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

package carpettisaddition.mixins.command.speedtest;

import carpettisaddition.commands.speedtest.skipcompression.PacketDeflaterWithNetworkSide;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketDeflater;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin
{
	@Shadow @Final private NetworkSide side;

	@ModifyExpressionValue(
			method = "setCompressionThreshold",
			at = @At(
					value = "NEW",
					target = "(I)Lnet/minecraft/network/PacketDeflater;"
			)
	)
	private PacketDeflater setPacketDeflaterNetworkSide(PacketDeflater deflater)
	{
		if (deflater instanceof PacketDeflaterWithNetworkSide)
		{
			((PacketDeflaterWithNetworkSide)deflater).setNetworkSide$TISCM(this.side);
		}
		return deflater;
	}
}
