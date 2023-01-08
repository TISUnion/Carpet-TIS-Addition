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

import carpettisaddition.mixins.network.ClientPlayNetworkHandlerAccessor;
import carpettisaddition.mixins.network.ServerPlayNetworkHandlerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;

import java.util.function.Consumer;

public class HandlerContext
{
	public static class S2C
	{
		public final ClientPlayNetworkHandler networkHandler;
		public final CompoundTag payload;
		public final MinecraftClient client;
		public final ClientPlayerEntity player;

		public S2C(ClientPlayNetworkHandler networkHandler, CompoundTag payload)
		{
			this.payload = payload;
			this.networkHandler = networkHandler;
			this.client = ((ClientPlayNetworkHandlerAccessor)networkHandler).getClient();
			this.player = this.client.player;
		}

		public void runSynced(Runnable runnable)
		{
			this.client.execute(runnable);
		}

		public void send(TISCMProtocol.C2S packetId, Consumer<CompoundTag> payloadBuilder)
		{
			TISCMClientPacketHandler.getInstance().sendPacket(packetId, payloadBuilder);
		}
	}

	public static class C2S
	{
		public final ServerPlayNetworkHandler networkHandler;
		public final CompoundTag payload;
		public final MinecraftServer server;
		public final ServerPlayerEntity player;
		public final String playerName;

		public C2S(ServerPlayNetworkHandler networkHandler, CompoundTag payload)
		{
			this.networkHandler = networkHandler;
			this.payload = payload;
			this.server = ((ServerPlayNetworkHandlerAccessor)networkHandler).getServer();
			this.player = this.networkHandler.player;
			this.playerName = this.player.getName().getString();
		}

		public void runSynced(Runnable runnable)
		{
			this.server.execute(runnable);
		}

		public void send(TISCMProtocol.S2C packetId, Consumer<CompoundTag> payloadBuilder)
		{
			TISCMServerPacketHandler.getInstance().sendPacket(this.networkHandler, packetId, payloadBuilder);
		}
	}
}
