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

package carpettisaddition.helpers.rule.chatMessageLengthLimitUnlocked;

import carpettisaddition.CarpetTISAdditionSettings;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.Utf8String;
import net.minecraft.network.codec.StreamCodec;

public class ChatMessageLengthLimitUnlockedHelper
{
	/**
	 * The client -> server packet size has a limit of 32KiB,
	 * so we use 32K for a bit size padding
	 */
	public static final int LIMIT_OVERRIDE = 32000;

	public static StreamCodec<ByteBuf, String> dynamicStringUtf8(StreamCodec<ByteBuf, String> original)
	{
		return new StreamCodec<>()
		{
			@Override
			public String decode(ByteBuf input)
			{
				if (!CarpetTISAdditionSettings.chatMessageLengthLimitUnlocked)
				{
					return original.decode(input);
				}
				return Utf8String.read(input, LIMIT_OVERRIDE);
			}

			@Override
			public void encode(ByteBuf output, String value)
			{
				if (!CarpetTISAdditionSettings.chatMessageLengthLimitUnlocked)
				{
					original.encode(output, value);
					return;
				}
				Utf8String.write(output, value, LIMIT_OVERRIDE);
			}
		};
	}
}
