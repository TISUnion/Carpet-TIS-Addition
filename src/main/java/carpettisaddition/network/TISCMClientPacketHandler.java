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

package carpettisaddition.network;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import carpettisaddition.utils.NbtUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Consumer;

public class TISCMClientPacketHandler
{
	private static final Logger LOGGER = CarpetTISAdditionServer.LOGGER;
	private static final TISCMClientPacketHandler INSTANCE = new TISCMClientPacketHandler();

	private final Map<TISCMProtocol.S2C, Consumer<HandlerContext.S2C>> handlers = new EnumMap<>(TISCMProtocol.S2C.class);
	private final Set<TISCMProtocol.C2S> serverSupportedPackets = Sets.newHashSet();

	private TISCMClientPacketHandler()
	{
		this.handlers.put(TISCMProtocol.S2C.HELLO, this::handleHello);
		this.handlers.put(TISCMProtocol.S2C.SUPPORTED_C2S_PACKETS, this::handleSupportPackets);
		this.handlers.put(TISCMProtocol.S2C.MSPT_METRICS_SAMPLE, this::handleMsptMetricsSample);
		if (this.handlers.size() < TISCMProtocol.S2C.ID_MAP.size())
		{
			throw new RuntimeException("TISCMServerPacketDispatcher doesn't handle all C2S packets");
		}
	}

	public static TISCMClientPacketHandler getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Invoked on main thread
	 */
	public void dispatch(ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf)
	{
		String packetId = packetByteBuf.readString();
		CompoundTag payload = packetByteBuf.readCompoundTag();
		TISCMProtocol.S2C.fromId(packetId).
				map(this.handlers::get).
				ifPresent( handler -> handler.accept(new HandlerContext.S2C(networkHandler, payload)));
	}

	public boolean isProtocolEnabled()
	{
		return !this.serverSupportedPackets.isEmpty();
	}

	public boolean doesServerSupport(TISCMProtocol.C2S packetId)
	{
		return packetId.isHandshake || this.serverSupportedPackets.contains(packetId);
	}

	public void sendPacket(TISCMProtocol.C2S packetId, Consumer<CompoundTag> payloadBuilder)
	{
		if (this.doesServerSupport(packetId))
		{
			Optional.ofNullable(MinecraftClient.getInstance().getNetworkHandler()).
					ifPresent(networkHandler -> networkHandler.sendPacket(packetId.packet(payloadBuilder)));
		}
	}

	/*
	 * -------------------------
	 *       Packet Senders
	 * -------------------------
	 */

	public void onConnectedToNewServer()
	{
		this.serverSupportedPackets.clear();
		sendPacket(TISCMProtocol.C2S.HI, nbt -> {
			nbt.putString("platform_name", TISCMProtocol.PLATFORM_NAME);
			nbt.putString("platform_version", TISCMProtocol.PLATFORM_VERSION);
		});
	}

	/*
	 * -------------------------
	 *       Packet Handlers
	 * -------------------------
	 */

	/*
	 * Handshake process:
	 * 1. client --hi--> server
	 * 2. client <-hello,packet_ids-- server
	 * 3. client --packet_ids-> server
	 */

	public void handleHello(HandlerContext.S2C ctx)
	{
		String platformName = ctx.payload.getString("platform_name");
		String platformVersion = ctx.payload.getString("platform_version");
		LOGGER.info("Serverside TISCM protocol supported with platform {} @ {}", platformName, platformVersion);

		List<String> ids = Lists.newArrayList(TISCMProtocol.S2C.ID_MAP.keySet());
		ctx.send(TISCMProtocol.C2S.SUPPORTED_S2C_PACKETS, nbt -> {
			nbt.put("supported_s2c_packets", NbtUtil.stringList2Nbt(ids));
		});
	}

	public void handleSupportPackets(HandlerContext.S2C ctx)
	{
		List<String> ids = NbtUtil.nbt2StringList(ctx.payload.getCompound("supported_c2s_packets"));
		LOGGER.debug("Serverside supported TISCM C2S packet ids: {}", ids);
		for (String id : ids)
		{
			TISCMProtocol.C2S.fromId(id).ifPresent(this.serverSupportedPackets::add);
		}
	}

	public void handleMsptMetricsSample(HandlerContext.S2C ctx)
	{
		ServerMsptMetricsDataSyncer.getInstance().receiveMetricData(ctx.payload);
	}
}
