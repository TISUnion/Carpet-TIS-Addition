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

package carpettisaddition.tests;

import carpettisaddition.utils.NetworkUtils;
import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import junit.framework.TestCase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.function.Consumer;

public class NetworkUtilsTest extends TestCase
{
	private static FriendlyByteBuf newBuffer()
	{
		return new FriendlyByteBuf(Unpooled.buffer());
	}

	private static void assertStyle(FriendlyByteBuf buf, NetworkUtils.NbtStyle expectedStyle)
	{
		NetworkUtils.NbtStyle nbtStyle = NetworkUtils.guessNbtStyle(buf);
		assertEquals(expectedStyle, nbtStyle);
	}

	private static void assertStyle(CompoundTag nbt, NetworkUtils.NbtStyle expectedStyle)
	{
		FriendlyByteBuf buf = newBuffer();
		buf.writeNbt(nbt);
		NetworkUtils.NbtStyle nbtStyle = NetworkUtils.guessNbtStyle(buf);
		assertEquals(expectedStyle, nbtStyle);
	}

	public void testNbtGuessStyle_valid()
	{
		List<Consumer<CompoundTag>> nbtMakers = Lists.newArrayList(
				nbt -> {},
				nbt -> nbt.putByte("byte", (byte)123),
				nbt -> nbt.putShort("short", (short)123),
				nbt -> nbt.putInt("int", 123),
				nbt -> nbt.putLong("long", 123),
				nbt -> nbt.putFloat("float", 123.45f),
				nbt -> nbt.putDouble("double", 123.45),
				nbt -> nbt.putByteArray("bytes", new byte[]{5, 6, 7}),
				nbt -> nbt.putString("string", "foobar"),

				nbt -> nbt.put("list", new ListTag()),
				nbt -> {
					ListTag child = new ListTag();
					CompoundTag e1 = new CompoundTag();
					CompoundTag e2 = new CompoundTag();
					e1.putLongArray("foo", new long[]{999, 888});
					child.add(e1);
					child.add(e2);
					nbt.put("list", child);
				},

				nbt -> nbt.put("compound", new CompoundTag()),
				nbt -> {
					CompoundTag child = new CompoundTag();
					child.putString("x", "X");
					child.putInt("y", -1);
					nbt.put("compound", child);
				},

				nbt -> nbt.putIntArray("ints", new int[]{5, 6, 7}),
				nbt -> nbt.putLongArray("longs", new long[]{5, 6, 7})
		);

		for (Consumer<CompoundTag> m1 : nbtMakers)
		{
			for (Consumer<CompoundTag> m2 : nbtMakers)
			{
				for (Consumer<CompoundTag> m3 : nbtMakers)
				{
					CompoundTag nbt = new CompoundTag();
					m1.accept(nbt);
					m2.accept(nbt);
					m3.accept(nbt);
					assertStyle(nbt, NetworkUtils.NbtStyle.CURRENT);
				}
			}
		}
	}

	public void testNbtGuessStyle_unknown()
	{
		// TODO: more tests for those UNKNOWN cases

		// buffer too small
		for (int i = 0; i < 2; i++)
		{
			FriendlyByteBuf buf = newBuffer();
			buf.writeBytes(new byte[i]);
			assertStyle(buf, NetworkUtils.NbtStyle.UNKNOWN);
		}

		{
			CompoundTag nbt = new CompoundTag();
			nbt.putString("foo", "abc");
			nbt.putInt("bar", 0);

			FriendlyByteBuf buf = newBuffer();
			buf.writeNbt(nbt);
			assertTrue(buf.readableBytes() >= 1);
			assertEquals(0x0A, buf.readByte());

			FriendlyByteBuf bufBad = newBuffer();
			bufBad.writeByte(0x0B);  // not 0x0A
			bufBad.writeBytes(buf);

			assertStyle(bufBad, NetworkUtils.NbtStyle.UNKNOWN);
		}
	}
}
