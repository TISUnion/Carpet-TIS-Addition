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

package carpettisaddition.utils;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;

public class NetworkUtil
{
	/**
	 * See <a href="https://wiki.vg/NBT">https://wiki.vg/NBT</a>
	 * for the nbt changes between mc < 1.20.2 and mc >= 1.20.2
	 */
	public enum NbtStyle
	{
		UNKNOWN,
		OLD,  // <  1.20.2
		NEW;  // >= 1.20.2

		public static final NbtStyle CURRENT =
				//#if MC >= 12002
				//$$ NEW;
				//#else
				OLD;
				//#endif
	}

	private static final int TAG_ID_COMPOUND = 0x0A;

	// Notes: reader index untouched
	public static NbtStyle guessNbtStyle(PacketByteBuf buf)
	{
		int n = buf.readableBytes();

		int prevReaderIndex = buf.readerIndex();
		try
		{
			if (n < 2)
			{
				return NbtStyle.UNKNOWN;
			}

			byte typeId = buf.readByte();
			if (typeId != TAG_ID_COMPOUND)
			{
				return NbtStyle.UNKNOWN;
			}

			if (n == 2)
			{
				if (buf.readByte() == 0)
				{
					// >=1.20.2, empty nbt
					return NbtStyle.NEW;
				}
				return NbtStyle.UNKNOWN;
			}
			else  // n > 2
			{
				byte[] bytes = new byte[2];
				buf.readBytes(bytes);

				// Double 0x00 for the empty root tag name
				if (bytes[0] == 0 && bytes[1] == 0)
				{
					return NbtStyle.OLD;
				}
				// A valid nbt type id
				else if (0 <= bytes[0] && bytes[0] < 13)
				{
					return NbtStyle.NEW;
				}
			}
		}
		finally
		{
			buf.readerIndex(prevReaderIndex);
		}

		return NbtStyle.UNKNOWN;
	}

	/**
	 * Read an NBT from a {@link PacketByteBuf}
	 *
	 * Compatible with both mc >= 1.20.2 and mc < 1.20.2 formats
	 */
	@Nullable
	public static NbtCompound readNbt(PacketByteBuf buf)
	{
		NbtStyle nbtStyle = guessNbtStyle(buf);

		if (NbtStyle.CURRENT == NbtStyle.OLD && nbtStyle == NbtStyle.NEW)
		{
			// I'm < mc1.20.2 (OLD), trying to read a nbt in NEW style

			//#if MC < 12002
			int prevReaderIndex = buf.readerIndex();
			PacketByteBuf tweakedBuf = new PacketByteBuf(Unpooled.buffer());
			tweakedBuf.writeByte(buf.readByte());  // 0x0A, tag type
			tweakedBuf.writeByte(0).writeByte(0);  // 2* 0x00
			tweakedBuf.writeBytes(buf);
			buf.readerIndex(prevReaderIndex);

			NbtCompound nbt = tweakedBuf.readNbt();

			int n = tweakedBuf.readerIndex();
			buf.readBytes(Math.max(0, n - 2));

			return nbt;
			//#endif
		}
		else if (NbtStyle.CURRENT == NbtStyle.NEW && nbtStyle == NbtStyle.OLD)
		{
			// I'm >= mc1.20.2 (NEW), trying to read a nbt in OLD style

			int prevReaderIndex = buf.readerIndex();
			PacketByteBuf tweakedBuf = new PacketByteBuf(Unpooled.buffer());
			tweakedBuf.writeByte(buf.readByte());  // 0x0A, tag type
			buf.readBytes(2);  // consume the 2* 0x00
			tweakedBuf.writeBytes(buf);
			buf.readerIndex(prevReaderIndex);

			NbtCompound nbt = tweakedBuf.readNbt();

			int n = tweakedBuf.readerIndex();
			buf.readBytes(Math.max(0, n > 1 ? n + 2 : n));
			return nbt;
		}

		return buf.readNbt();
	}
}
