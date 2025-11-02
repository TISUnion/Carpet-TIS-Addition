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

import carpettisaddition.utils.NetworkUtils;
import carpettisaddition.utils.compat.CustomPacketPayload;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;

//#if MC >= 12005
//$$ import net.minecraft.network.codec.StreamCodec;
//$$ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//#endif

public class TISCMCustomPayload implements CustomPacketPayload
{
	public static final ResourceLocation ID = TISCMProtocol.CHANNEL;

	//#if MC >= 12005
	//$$ public static final CustomPacketPayload.Type<TISCMCustomPayload> KEY = new CustomPacketPayload.Type<>(ID);
	//$$ public static final StreamCodec<FriendlyByteBuf, TISCMCustomPayload> CODEC = CustomPacketPayload.codec(TISCMCustomPayload::write, TISCMCustomPayload::new);
	//#endif

	private final String packetId;
	private final CompoundTag nbt;

	public TISCMCustomPayload(String packetId, CompoundTag nbt)
	{
		this.packetId = packetId;
		this.nbt = nbt;
	}

	public TISCMCustomPayload(FriendlyByteBuf buf)
	{
		this(
				buf.readUtf(
						//#if MC < 11700
						Short.MAX_VALUE
						//#endif
				),
				NetworkUtils.readNbt(buf)
		);
	}

	//#if MC < 12005
	@Override
	//#endif
	public void write(FriendlyByteBuf buf)
	{
		buf.writeUtf(this.packetId);
		buf.writeNbt(this.nbt);
	}

	//#if MC < 12005
	@Override
	//#endif
	public ResourceLocation id()
	{
		return ID;
	}

	//#if MC >= 12005
	//$$ @Override
	//$$ public Type<? extends CustomPacketPayload> type()
	//$$ {
	//$$ 	return KEY;
	//$$ }
	//#endif

	public String getPacketId()
	{
		return packetId;
	}

	public CompoundTag getNbt()
	{
		return nbt;
	}
}