package carpettisaddition.network;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataStorage;
import carpettisaddition.utils.NbtUtil;
import carpettisaddition.utils.ReflectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static carpettisaddition.utils.MiscUtil.assertTrue;

public class TISCMClientPacketHandler
{
	private static final Logger LOGGER = CarpetTISAdditionServer.LOGGER;
	private static final TISCMClientPacketHandler INSTANCE = new TISCMClientPacketHandler();

	private final Map<TISCMProtocol.S2C, Consumer<HandlerContext.S2C>> handlers = new EnumMap<>(TISCMProtocol.S2C.class);
	private final Set<TISCMProtocol.C2S> serverSupportedPackets = Sets.newHashSet();

	private TISCMClientPacketHandler()
	{
		Arrays.asList(this.getClass().getDeclaredMethods()).forEach(method ->
				Optional.ofNullable(method.getAnnotation(Handler.class)).ifPresent(a -> {
					BiFunction<Object, Object[], Object> invoker = ReflectionUtil.invoker(method);
					assertTrue( method.getParameterCount() == 1 && method.getParameters()[0].getType() == HandlerContext.S2C.class);
					this.handlers.put(a.value(), ctx -> invoker.apply(this, new Object[]{ctx}));
				})
		);
		if (this.handlers.size() < TISCMProtocol.S2C.ID_MAP.size())
		{
			throw new RuntimeException("TISCMServerPacketDispatcher doesn't handle all C2S packets");
		}
	}

	public static TISCMClientPacketHandler getInstance()
	{
		return INSTANCE;
	}

	public void dispatch(ClientPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf)
	{
		TISCMProtocol.S2C.fromId(packetByteBuf.readString()).
				map(this.handlers::get).
				ifPresent( handler -> handler.accept(new HandlerContext.S2C(networkHandler, packetByteBuf)));
	}

	public boolean doesServerSupport(TISCMProtocol.C2S packetId)
	{
		return packetId.isHandshake || this.serverSupportedPackets.contains(packetId);
	}

	public void sendPacket(TISCMProtocol.C2S packetId, Consumer<PacketByteBuf> byteBufBuilder)
	{
		if (this.doesServerSupport(packetId))
		{
			Optional.ofNullable(MinecraftClient.getInstance().getNetworkHandler()).
					ifPresent(networkHandler -> networkHandler.sendPacket(packetId.packet(byteBufBuilder)));
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
		sendPacket(TISCMProtocol.C2S.HI, buf -> buf.
				writeString(TISCMProtocol.PLATFORM_NAME).
				writeString(TISCMProtocol.PLATFORM_VERSION)
		);
	}

	/*
	 * -------------------------
	 *       Packet Handlers
	 * -------------------------
	 */

	@Retention(RetentionPolicy.RUNTIME)
	private @interface Handler
	{
		TISCMProtocol.S2C value();
	}

	/*
	 * Handshake process:
	 * 1. client --hi--> server
	 * 2. client <-hello,packet_ids-- server
	 * 3. client --packet_ids-> server
	 */

	@Handler(TISCMProtocol.S2C.HELLO)
	public void handleHello(HandlerContext.S2C ctx)
	{
		String platformName = ctx.buf.readString();
		String platformVersion = ctx.buf.readString();
		LOGGER.info("Serverside TISCM protocol supported with platform {} @ {} detected", platformName, platformVersion);

		List<String> ids = Lists.newArrayList(TISCMProtocol.S2C.ID_MAP.keySet());
		ctx.send(TISCMProtocol.C2S.SUPPORTED_S2C_PACKETS, buf -> buf.
				writeCompoundTag(NbtUtil.stringList2Nbt(ids))
		);
	}

	@Handler(TISCMProtocol.S2C.SUPPORTED_C2S_PACKETS)
	public void handleSupportPackets(HandlerContext.S2C ctx)
	{
		List<String> ids = NbtUtil.nbt2StringList(Objects.requireNonNull(ctx.buf.readCompoundTag()));
		LOGGER.info("Serverside supported TISCM C2S packet ids: {}", ids);
		ctx.runSynced(() -> {
			for (String id : ids)
			{
				TISCMProtocol.C2S.fromId(id).ifPresent(this.serverSupportedPackets::add);
			}
		});
	}

	@Handler(value = TISCMProtocol.S2C.MSPT_METRICS_SAMPLE)
	public void handleMsptMetricsSample(HandlerContext.S2C ctx)
	{
		long msThisTick = ctx.buf.readLong();
		ctx.runSynced(() -> ServerMsptMetricsDataStorage.getInstance().receiveMetricData(msThisTick));
	}
}
