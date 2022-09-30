package carpettisaddition.network;

import carpettisaddition.mixins.network.ClientPlayNetworkHandlerAccessor;
import carpettisaddition.mixins.network.ServerPlayNetworkHandlerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
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
		public final PacketByteBuf buf;
		public final MinecraftClient client;
		public final ClientPlayerEntity player;

		public S2C(ClientPlayNetworkHandler networkHandler, PacketByteBuf buf)
		{
			this.buf = buf;
			this.networkHandler = networkHandler;
			this.client = ((ClientPlayNetworkHandlerAccessor)networkHandler).getClient();
			this.player = this.client.player;
		}

		public void runSynced(Runnable runnable)
		{
			this.client.execute(runnable);
		}

		public void send(TISCMProtocol.C2S packetId, Consumer<PacketByteBuf> byteBufBuilder)
		{
			TISCMClientPacketHandler.getInstance().sendPacket(packetId, byteBufBuilder);
		}
	}

	public static class C2S
	{
		public final ServerPlayNetworkHandler networkHandler;
		public final PacketByteBuf buf;
		public final MinecraftServer server;
		public final ServerPlayerEntity player;
		public final String playerName;

		public C2S(ServerPlayNetworkHandler networkHandler, PacketByteBuf buf)
		{
			this.networkHandler = networkHandler;
			this.buf = buf;
			this.server = ((ServerPlayNetworkHandlerAccessor)networkHandler).getServer();
			this.player = this.networkHandler.player;
			this.playerName = this.player.getName().getString();
		}

		public void runSynced(Runnable runnable)
		{
			this.server.execute(runnable);
		}

		public void send(TISCMProtocol.S2C packetId, Consumer<PacketByteBuf> byteBufBuilder)
		{
			TISCMServerPacketHandler.getInstance().sendPacket(this.networkHandler, packetId, byteBufBuilder);
		}
	}
}
