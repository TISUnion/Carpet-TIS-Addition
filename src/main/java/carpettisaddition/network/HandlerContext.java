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
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Consumer;

public class HandlerContext
{
	public static class S2C
	{
		public final ClientPacketListener networkHandler;
		public final CompoundTag payload;
		public final Minecraft client;
		public final LocalPlayer player;

		public S2C(ClientPacketListener networkHandler, CompoundTag payload)
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
		public final ServerGamePacketListenerImpl networkHandler;
		public final CompoundTag payload;
		public final MinecraftServer server;
		public final ServerPlayer player;
		public final String playerName;

		public C2S(ServerGamePacketListenerImpl networkHandler, CompoundTag payload)
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
