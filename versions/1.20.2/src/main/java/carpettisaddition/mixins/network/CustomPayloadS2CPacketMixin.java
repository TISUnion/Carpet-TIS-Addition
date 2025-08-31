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

package carpettisaddition.mixins.network;

import carpettisaddition.network.TISCMCustomPayload;
import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CustomPayloadS2CPacket.class)
public abstract class CustomPayloadS2CPacketMixin
{
	@ModifyExpressionValue(
			method = "<clinit>",
			at = @At(
					value = "INVOKE",
					target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;"
			)
	)
	private static ImmutableMap.Builder<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>> registerTISCMS2CPackets(ImmutableMap.Builder<Identifier, PacketByteBuf.PacketReader<? extends CustomPayload>> builder)
	{
		builder.put(TISCMCustomPayload.ID, TISCMCustomPayload::new);
		return builder;
	}
}
