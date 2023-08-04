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

import carpettisaddition.utils.compat.CustomPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class TISCMCustomPayload implements CustomPayload
{
	public static final Identifier ID = TISCMProtocol.CHANNEL;

	private final String packetId;
	private final CompoundTag nbt;

	public TISCMCustomPayload(String packetId, CompoundTag nbt)
	{
		this.packetId = packetId;
		this.nbt = nbt;
	}

	public TISCMCustomPayload(PacketByteBuf buf)
	{
		this(buf.readString(), buf.readCompoundTag());
	}

	@Override
	public void write(PacketByteBuf buf)
	{
		buf.writeString(this.packetId);
		buf.writeCompoundTag(this.nbt);
	}

	@Override
	public Identifier id()
	{
		return ID;
	}

	public String getPacketId()
	{
		return packetId;
	}

	public CompoundTag getNbt()
	{
		return nbt;
	}
}