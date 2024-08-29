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
import carpettisaddition.commands.speedtest.skipcompression.SpeedTestCompressionSkipper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketDeflater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PacketDeflater.class)
public abstract class PacketDeflaterMixin implements PacketDeflaterWithNetworkSide
{
	@Unique
	private NetworkSide networkSide$TISCM = null;

	@Override
	public void setNetworkSide$TISCM(NetworkSide networkSide)
	{
		this.networkSide$TISCM = networkSide;
	}

	@Override
	public NetworkSide getNetworkSide$TISCM()
	{
		return this.networkSide$TISCM;
	}

	/**
	 * Detect speed test payload to cancelling the compression, for less CPU usage
	 */
	@ModifyExpressionValue(
			method = "encode(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Lio/netty/buffer/ByteBuf;)V",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/network/PacketDeflater;compressionThreshold:I",
					ordinal = 0
			)
	)
	private int dontCompressSpeedTestPayloadPackets(int compressionThreshold, @Local(argsOnly = true, ordinal = 0) ByteBuf bufInput)
	{
		if (SpeedTestCompressionSkipper.isSpeedTestPayloadPacket(this, bufInput))
		{
			return Integer.MAX_VALUE;
		}
		return compressionThreshold;
	}
}
