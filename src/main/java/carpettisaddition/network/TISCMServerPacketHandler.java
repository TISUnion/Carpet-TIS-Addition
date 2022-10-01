package carpettisaddition.network;

import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.helpers.rule.syncServerMsptMetricsData.ServerMsptMetricsDataSyncer;
import carpettisaddition.utils.NbtUtil;
import carpettisaddition.utils.ReflectionUtil;
import com.google.common.collect.Lists;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.PacketByteBuf;
import org.apache.logging.log4j.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static carpettisaddition.utils.MiscUtil.assertTrue;

public class TISCMServerPacketHandler
{
	private static final Logger LOGGER = CarpetTISAdditionServer.LOGGER;
	private static final TISCMServerPacketHandler INSTANCE = new TISCMServerPacketHandler();

	private final Map<TISCMProtocol.C2S, Consumer<HandlerContext.C2S>> handlers = new EnumMap<>(TISCMProtocol.C2S.class);
	private final Map<ServerPlayNetworkHandler, Set<TISCMProtocol.S2C>> clientSupportedPacketsMap = new WeakHashMap<>();

	private TISCMServerPacketHandler()
	{
		Arrays.asList(this.getClass().getDeclaredMethods()).forEach(method ->
				Optional.ofNullable(method.getAnnotation(Handler.class)).ifPresent(a -> {
					BiFunction<Object, Object[], Object> invoker = ReflectionUtil.invoker(method);
					assertTrue( method.getParameterCount() == 1 && method.getParameters()[0].getType() == HandlerContext.C2S.class);
					this.handlers.put(a.value(), ctx -> invoker.apply(this, new Object[]{ctx}));
				})
		);
		if (this.handlers.size() < TISCMProtocol.C2S.ID_MAP.size())
		{
			throw new RuntimeException("TISCMServerPacketDispatcher doesn't handle all C2S packets");
		}
	}

	public static TISCMServerPacketHandler getInstance()
	{
		return INSTANCE;
	}

	public void dispatch(ServerPlayNetworkHandler networkHandler, PacketByteBuf packetByteBuf)
	{
		TISCMProtocol.C2S.fromId(packetByteBuf.readString(Short.MAX_VALUE)).
				map(this.handlers::get).
				ifPresent( handler -> handler.accept(new HandlerContext.C2S(networkHandler, packetByteBuf)));
	}

	public boolean doesClientSupport(ServerPlayNetworkHandler networkHandler, TISCMProtocol.S2C packetId)
	{
		if (packetId.isHandshake)
		{
			return true;
		}
		Set<TISCMProtocol.S2C> packetIds = this.clientSupportedPacketsMap.get(networkHandler);
		return packetIds != null && packetIds.contains(packetId);
	}

	public void sendPacket(ServerPlayNetworkHandler networkHandler, TISCMProtocol.S2C packetId, Consumer<PacketByteBuf> byteBufBuilder)
	{
		if (this.doesClientSupport(networkHandler, packetId))
		{
			networkHandler.sendPacket(packetId.packet(byteBufBuilder));
		}
	}

	private void broadcast(TISCMProtocol.S2C packetId, Consumer<PacketByteBuf> byteBufBuilder)
	{
		this.clientSupportedPacketsMap.forEach((serverPlayNetworkHandler, supportedPackets) -> {
			this.sendPacket(serverPlayNetworkHandler, packetId, byteBufBuilder);
		});
	}

	/*
	 * -------------------------
	 *       Packet Senders
	 * -------------------------
	 */

	/*
	 * -------------------------
	 *       Packet Handlers
	 * -------------------------
	 */

	@Retention(RetentionPolicy.RUNTIME)
	private @interface Handler
	{
		TISCMProtocol.C2S value();
	}

	@Handler(TISCMProtocol.C2S.HI)
	public void handleHi(HandlerContext.C2S ctx)
	{
		String platformName = ctx.buf.readString(Short.MAX_VALUE);
		String platformVersion = ctx.buf.readString(Short.MAX_VALUE);
		LOGGER.info("Player {} connected with TISCM protocol support ({} @ {})", ctx.playerName, platformName, platformVersion);

		ctx.send(TISCMProtocol.S2C.HELLO, buf -> buf.
				writeString(TISCMProtocol.PLATFORM_NAME).
				writeString(TISCMProtocol.PLATFORM_VERSION)
		);

		List<String> ids = Lists.newArrayList(TISCMProtocol.C2S.ID_MAP.keySet());
		ctx.send(TISCMProtocol.S2C.SUPPORTED_C2S_PACKETS, buf -> buf.
				writeCompoundTag(NbtUtil.stringList2Nbt(ids))
		);
	}

	@Handler(value = TISCMProtocol.C2S.SUPPORTED_S2C_PACKETS)
	public void handleSupportPackets(HandlerContext.C2S ctx)
	{
		List<String> ids = NbtUtil.nbt2StringList(Objects.requireNonNull(ctx.buf.readCompoundTag()));
		LOGGER.debug("Player {} clientside supported TISCM S2C packet ids: {}", ctx.playerName, ids);
		Set<TISCMProtocol.S2C> packetIds = ids.stream().
				map(TISCMProtocol.S2C::fromId).
				filter(Optional::isPresent).
				map(Optional::get).
				collect(Collectors.toSet());
		ctx.runSynced(() -> this.clientSupportedPacketsMap.put(ctx.networkHandler, packetIds));
	}

	@Handler(value = TISCMProtocol.C2S.MSPT_METRICS_SUBSCRIBE)
	public void handleMsptMetricsSubscribe(HandlerContext.C2S ctx)
	{
		boolean	subscribe = ctx.buf.readBoolean();
		LOGGER.debug("{} MSPT_METRICS_SUBSCRIBE {}", ctx.playerName, subscribe);
		Consumer<ServerPlayNetworkHandler> consumer = subscribe ?
				ServerMsptMetricsDataSyncer.getInstance()::addClient :
				ServerMsptMetricsDataSyncer.getInstance()::removeClient;
		ctx.runSynced(() -> consumer.accept(ctx.networkHandler));
	}
}
