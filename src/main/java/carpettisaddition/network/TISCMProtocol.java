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

import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.utils.RegistryUtil;
import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

//#if MC >= 12002
//$$ import java.util.function.Function;
//#endif

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

		public CustomPayloadC2SPacket packet(Consumer<CompoundTag> payloadBuilder)
		{
			return makePacket(CustomPayloadC2SPacket::new, this, payloadBuilder);
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

		MSPT_METRICS_SAMPLE,  // syncServerMsptMetricsData
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

		public CustomPayloadS2CPacket packet(Consumer<CompoundTag> payloadBuilder)
		{
			return makePacket(CustomPayloadS2CPacket::new, this, payloadBuilder);
		}
	}

	public interface PacketId
	{
		String name();  // implemented in enum
		default String getId() { return this.name().toLowerCase(); }
	}

	private static <T> T makePacket(
			//#if MC >= 12002
			//$$ Function<TISCMCustomPayload, T> packetConstructor,
			//#else
			BiFunction<Identifier, PacketByteBuf, T> packetConstructor,
			//#endif
			PacketId packetId, Consumer<CompoundTag> payloadBuilder
	)
	{
		CompoundTag nbt = new CompoundTag();
		payloadBuilder.accept(nbt);

		//#if MC >= 12002
		//$$ return packetConstructor.apply(new TISCMCustomPayload(packetId.getId(), nbt));
		//#else
		PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
		packetByteBuf.writeString(packetId.getId());
		packetByteBuf.writeCompoundTag(nbt);
		return packetConstructor.apply(TISCMProtocol.CHANNEL, packetByteBuf);
		//#endif
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
