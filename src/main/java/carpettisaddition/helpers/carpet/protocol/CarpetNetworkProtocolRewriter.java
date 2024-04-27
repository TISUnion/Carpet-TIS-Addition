/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.helpers.carpet.protocol;

import carpettisaddition.CarpetTISAdditionMod;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketByteBuf;

public class CarpetNetworkProtocolRewriter
{
	private static void drain(PacketByteBuf buf)
	{
		buf.readBytes(buf.readableBytes());
	}

	// if bufVersion == targetVersion, return the input buf directly
	// otherwise, return a new rewritten buf, and the input buf will be drained
	public static PacketByteBuf rewrite(PacketByteBuf buf, CarpetNetworkProtocolVersion targetVersion)
	{
		CarpetTISAdditionMod.LOGGER.debug("CarpetNetworkProtocolRewriter rewrite start, target={}", targetVersion);

		UniversalCarpetPayload payload = new UniversalCarpetPayload(buf);
		CarpetNetworkProtocolVersion bufVersion = payload.getVersion();
		if (bufVersion == CarpetNetworkProtocolVersion.UNKNOWN || bufVersion == targetVersion)
		{
			return buf;
		}

		CarpetTISAdditionMod.LOGGER.debug("CarpetNetworkProtocolRewriter rewriting {} -> {}", bufVersion, targetVersion);

		drain(buf);

		PacketByteBuf heapBuf = new PacketByteBuf(Unpooled.buffer());
		payload.writeTo(heapBuf, targetVersion);
		return heapBuf;
	}
}
