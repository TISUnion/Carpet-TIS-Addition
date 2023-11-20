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

package carpettisaddition.commands.speedtest;

import net.minecraft.util.Util;

import java.util.Random;

public class SpeedTestPacketUtils
{
	/**
	 * 16KiB per payload packet
	 * Notes for packet size limit: C2S = 32KiB, S2C = 1MiB
	 */
	public static final int SIZE_PER_PACKET = 1024 * 16;

	private static final byte[] PAYLOAD_BUFFER = Util.make(() -> {
		// the magic number 60 is to make sure the whole mc packet (len + (packetId + packetBuf)) equals to SIZE_PER_PACKET
		final byte[] result = new byte[SIZE_PER_PACKET - 60];
		new Random(42).nextBytes(result);
		return result;
	});

	public static byte[] getPayloadByteArray()
	{
		return PAYLOAD_BUFFER;
	}

	public static int getPacketCount(int testSizeMb)
	{
		long bytes = ((long)testSizeMb) << 20;
		if (bytes % SpeedTestPacketUtils.SIZE_PER_PACKET != 0)
		{
			throw new IllegalStateException("bad packet division");
		}
		long cnt = bytes / SpeedTestPacketUtils.SIZE_PER_PACKET;
		if (cnt > Integer.MAX_VALUE)
		{
			throw new IllegalArgumentException("test size too large");
		}
		return (int)cnt;
	}
}
