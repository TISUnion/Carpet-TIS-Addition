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
import carpettisaddition.utils.NbtUtils;
import carpettisaddition.utils.NetworkUtils;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Objects;

class UniversalCarpetPayload
{
	private static final int V1_DATA = 1;
	private static final int V1_HI = 69;
	private static final int V1_HELLO = 420;
	private static final String V2_HI = "69";
	private static final String V2_HELLO = "420";

	public int action;  // v1 id
	public String string;  // v1 HI / HELLO
	public CompoundTag nbt;  // v1 DATA, v2 all
	public CarpetNetworkProtocolVersion version;

	// Notes: buf readerIndex will remain unchanged
	public UniversalCarpetPayload(FriendlyByteBuf buf)
	{
		this.action = -1;
		this.string = null;
		this.nbt = null;
		this.version = CarpetNetworkProtocolVersion.UNKNOWN;

		int prevReaderIndex;

		// try v1
		prevReaderIndex = buf.readerIndex();
		try
		{
			// v1 format
			this.action = buf.readVarInt();
			if (this.action == V1_HI || this.action == V1_HELLO)
			{
				this.string = buf.readUtf(64);
			}
			else if (this.action == V1_DATA)
			{
				this.nbt = Objects.requireNonNull(NetworkUtils.readNbt(buf));
			}
			else
			{
				throw new Exception("unknown action " + this.action);
			}
			if (buf.readableBytes() != 0)
			{
				throw new Exception(String.format("too many data, action=%d remained=%d", this.action, buf.readableBytes()));
			}

			this.version = CarpetNetworkProtocolVersion.V1;
			CarpetTISAdditionMod.LOGGER.debug("UniversalCarpetPayload read V1 success, action={} str={} nbt={}", this.action, this.string, this.nbt);
			return;
		}
		catch (Exception e)
		{
			CarpetTISAdditionMod.LOGGER.debug("UniversalCarpetPayload read V1 failed, err={}", e.toString());
		}
		finally
		{
			buf.readerIndex(prevReaderIndex);
		}

		// try v2
		prevReaderIndex = buf.readerIndex();
		try
		{
			this.action = -1;
			this.string = null;
			this.nbt = NetworkUtils.readNbt(buf);
			if (buf.readableBytes() != 0)
			{
				throw new Exception(String.format("too many data, nbt=%s remained=%d", this.nbt, buf.readableBytes()));
			}

			if (this.nbt == null)
			{
				this.nbt = new CompoundTag();
			}
			this.version = CarpetNetworkProtocolVersion.V2;
			CarpetTISAdditionMod.LOGGER.debug("UniversalCarpetPayload read V2 success, nbt={}", this.nbt);
			return;
		}
		catch (Exception e)
		{
			CarpetTISAdditionMod.LOGGER.debug("UniversalCarpetPayload read V2 failed, err={}", e.toString());
		}
		finally
		{
			buf.readerIndex(prevReaderIndex);
		}
	}

	public CarpetNetworkProtocolVersion getVersion()
	{
		return this.version;
	}

	public void writeTo(FriendlyByteBuf buf, CarpetNetworkProtocolVersion targetVersion)
	{
		switch (targetVersion)
		{
			case V1:
				this.assertVersion(CarpetNetworkProtocolVersion.V2);
				Objects.requireNonNull(this.nbt);

				List<String> keys = Lists.newArrayList(this.nbt.getAllKeys());
				if (keys.size() == 1)  // for v2 HI / HELLO, there's only 1 key in the nbt
				{
					String id = keys.get(0);
					if (id.equals(V2_HI) || id.equals(V2_HELLO))
					{
						// v1 carpet hi / hello, format: varint (69 or 420) + string
						CarpetTISAdditionMod.LOGGER.debug("UniversalCarpetPayload write V1 HI / HELLO, id={}", id);
						buf.writeVarInt(Integer.parseInt(id));
						buf.writeUtf(NbtUtils.getStringOrEmpty(this.nbt, id));
						return;
					}
				}

				// v1 carpet data, format: varint + nbt
				CarpetTISAdditionMod.LOGGER.debug("UniversalCarpetPayload write V1 DATA, nbt={}", this.nbt);
				buf.writeVarInt(V1_DATA);
				buf.writeNbt(this.nbt);
				break;

			case V2:
				this.assertVersion(CarpetNetworkProtocolVersion.V1);

				CompoundTag tagToWrite = this.nbt;
				if (this.action == V1_HI || this.action == V1_HELLO)
				{
					tagToWrite = new CompoundTag();
					tagToWrite.putString(String.valueOf(this.action), Objects.requireNonNull(this.string));
				}

				CarpetTISAdditionMod.LOGGER.debug("UniversalCarpetPayload write V2, action={}, tag={}", this.action, tagToWrite);
				buf.writeNbt(Objects.requireNonNull(tagToWrite));
				break;
		}
	}

	private void assertVersion(CarpetNetworkProtocolVersion version)
	{
		if (this.version != version)
		{
			throw new RuntimeException(String.format("Unexpected protocol version, expected %s, found %s", version, this.version));
		}
	}
}
