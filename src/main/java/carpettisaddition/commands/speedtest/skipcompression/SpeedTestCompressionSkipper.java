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

package carpettisaddition.commands.speedtest.skipcompression;

import carpettisaddition.commands.speedtest.SpeedTestPacketUtils;
import carpettisaddition.network.TISCMProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.Util;

import java.util.Arrays;
import java.util.Objects;

//#if MC >= 12005
//$$ import carpettisaddition.mixins.command.speedtest.PacketCodecDispatcherAccessor;
//$$ import net.minecraft.network.RegistryByteBuf;
//$$ import net.minecraft.network.packet.CommonPackets;
//$$ import net.minecraft.network.state.PlayStateFactories;
//#endif

public class SpeedTestCompressionSkipper
{
	private static final byte[] DOWNLOAD_PACKET_BYTES_PREFIX = Util.make(() -> {
		TISCMProtocol.S2C packetId = TISCMProtocol.S2C.SPEED_TEST_DOWNLOAD_PAYLOAD;
		return makeBytes(getMinecraftPacketId(PacketFlow.CLIENTBOUND, packetId.packet(nbt -> {})), packetId.getId());
	});

	private static final byte[] UPLOAD_PACKET_BYTES_PREFIX = Util.make(() -> {
		TISCMProtocol.C2S packetId = TISCMProtocol.C2S.SPEED_TEST_UPLOAD_PAYLOAD;
		Packet<?> examplePacket =
				//#if MC >= 11700
				//$$ packetId.packet(nbt -> {});
				//#else
				// the constructor used in TISCMProtocol.C2S#packet is not available on the serverside
				new net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket();
				//#endif
		return makeBytes(getMinecraftPacketId(PacketFlow.SERVERBOUND, examplePacket), packetId.getId());
	});

	private static byte[] makeBytes(int mcPacketId, String tiscmPacketId)
	{
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeVarInt(mcPacketId);
		buf.writeResourceLocation(TISCMProtocol.CHANNEL);
		buf.writeUtf(tiscmPacketId);

		byte[] result = new byte[buf.readableBytes()];
		buf.readBytes(result);
		buf.release();
		return result;
	}

	private static int getMinecraftPacketId(PacketFlow side, Packet<?> packet)
	{
		try
		{
			//#if MC >= 12005
			//$$ // side == server means receiving c2s packet
			//$$ //#if MC >= 12105
			//$$ //$$ var codec = switch (side)
			//$$ //$$ {
			//$$ //$$ 	case SERVERBOUND -> PlayStateFactories.C2S.bind(buf -> new RegistryByteBuf(buf, null), () -> true).codec();
			//$$ //$$ 	case CLIENTBOUND -> PlayStateFactories.S2C.bind(buf -> new RegistryByteBuf(buf, null)).codec();
			//$$ //$$ };
			//$$ //#else
			//$$ var factory = side == NetworkSide.SERVERBOUND ? PlayStateFactories.C2S : PlayStateFactories.S2C;
			//$$ var codec = factory.bind(buf -> new RegistryByteBuf(buf, null)).codec();
			//$$ //#endif
			//$$ var type = side == NetworkSide.SERVERBOUND ? CommonPackets.CUSTOM_PAYLOAD_C2S : CommonPackets.CUSTOM_PAYLOAD_S2C;
			//$$ if (codec instanceof PacketCodecDispatcherAccessor<?> packetCodecDispatcher)
			//$$ {
			//$$ 	var packetId = packetCodecDispatcher.getTypeToIndex().getOrDefault(type, -1);
			//$$ 	if (packetId == -1)
			//$$ 	{
			//$$ 		throw new IndexOutOfBoundsException("Failed to get packet id for %s from %s".formatted(type, packetCodecDispatcher));
			//$$ 	}
			//$$ 	return packetId;
			//$$ }
			//$$ else
			//$$ {
			//$$ 	throw new RuntimeException("codec is not a PacketCodecDispatcherAccessor");
			//$$ }
			//#elseif MC >= 12002
			//$$ return NetworkState.PLAY.getHandler(side).getId(packet);
			//#else
			return Objects.requireNonNull(ConnectionProtocol.PLAY.getPacketId(side, packet));
			//#endif
		}
		catch (Exception e)
		{
			throw new RuntimeException("failed to get " + side + " packet id of " + packet);
		}
	}

	public static boolean isSpeedTestPayloadPacket(PacketDeflaterWithNetworkSide deflater, ByteBuf byteBuf)
	{
		PacketFlow side = deflater.getNetworkSide$TISCM();
		if (side == null)
		{
			return false;
		}

		byte[] prefix;
		switch (side)
		{
			case SERVERBOUND:  // I'm the server, deflate for S->C, download
				prefix = DOWNLOAD_PACKET_BYTES_PREFIX;
				break;
			case CLIENTBOUND:  // I'm the client, deflate for C->S, upload
				prefix = UPLOAD_PACKET_BYTES_PREFIX;
				break;
			default:
				return false;
		}

		byteBuf = byteBuf.slice();
		int n = byteBuf.readableBytes();
		if (n >= prefix.length && n >= SpeedTestPacketUtils.SIZE_PER_PACKET - 200)
		{
			byte[] sample = new byte[prefix.length];
			byteBuf.readBytes(sample);
			return Arrays.equals(sample, prefix);
		}

		return false;
	}
}
