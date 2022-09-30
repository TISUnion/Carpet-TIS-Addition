package carpettisaddition.network;

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.utils.RegistryUtil;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class TISCMProtocol
{
	public static final String ID = "tiscm";
	public static final String PLATFORM_NAME = CarpetTISAdditionMod.MOD_NAME;
	public static final String PLATFORM_VERSION = CarpetTISAdditionMod.getVersion();
	public static final Identifier CHANNEL = RegistryUtil.id(ID, "network/v1");

	public enum C2S implements PacketId
	{
		HI(true),
		SUPPORTED_S2C_PACKETS(true),
		MSPT_METRICS_SUBSCRIBE,
		;

		public static final Map<String, C2S> ID_MAP = createIdMap(values());
		public final boolean isHandshake;

		C2S(boolean isHandshake)
		{
			this.isHandshake = isHandshake;
		}

		C2S() { this(false); }

		public static Optional<C2S> fromId(String id)
		{
			return Optional.ofNullable(ID_MAP.get(id));
		}

		public CustomPayloadC2SPacket packet(Consumer<PacketByteBuf> byteBufBuilder)
		{
			return makePacket(CustomPayloadC2SPacket::new, this, byteBufBuilder);
		}

		public boolean isSupported()
		{
			return TISCMClientPacketHandler.getInstance().doesServerSupport(this);
		}
	}

	public enum S2C implements PacketId
	{
		HELLO(true),
		SUPPORTED_C2S_PACKETS(true),
		MSPT_METRICS_SAMPLE,
		;

		public static final Map<String, S2C> ID_MAP = createIdMap(values());
		public final boolean isHandshake;

		S2C(boolean isHandshake)
		{
			this.isHandshake = isHandshake;
		}

		S2C() { this(false); }

		public static Optional<S2C> fromId(String id)
		{
			return Optional.ofNullable(ID_MAP.get(id));
		}

		public CustomPayloadS2CPacket packet(Consumer<PacketByteBuf> byteBufBuilder)
		{
			return makePacket(CustomPayloadS2CPacket::new, this, byteBufBuilder);
		}
	}

	public interface PacketId
	{
		String name();  // implemented in enum
		default String getId() { return this.name().toLowerCase(); }
	}

	private static <T> T makePacket(BiFunction<Identifier, PacketByteBuf, T> packetConstructor, PacketId packetId, Consumer<PacketByteBuf> byteBufBuilder)
	{
		PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
		packetByteBuf.writeString(packetId.getId());
		byteBufBuilder.accept(packetByteBuf);
		return packetConstructor.apply(TISCMProtocol.CHANNEL, packetByteBuf);
	}

	private static <T extends PacketId> Map<String, T> createIdMap(T[] values)
	{
		Map<String, T> idMap = Maps.newLinkedHashMap();
		for (T value : values)
		{
			idMap.put(value.getId(), value);
		}
		return idMap;
	}
}
