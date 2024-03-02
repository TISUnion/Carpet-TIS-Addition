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

package carpettisaddition.mixins.carpet.tweaks.robustness.network;

import carpet.network.CarpetClient;
import carpettisaddition.helpers.carpet.protocol.CarpetNetworkProtocolRewriter;
import carpettisaddition.helpers.carpet.protocol.CarpetNetworkProtocolVersion;
import carpettisaddition.utils.NetworkUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CarpetClient.CarpetPayload.class)
public abstract class CarpetPayloadMixin
{
	@ModifyVariable(
			method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
			at = @At("HEAD"),
			argsOnly = true
	)
	private static PacketByteBuf carpetProtocolCompatibilityFix_fixIncomingPacket(PacketByteBuf buf)
	{
		return CarpetNetworkProtocolRewriter.rewrite(
				buf,
				CarpetNetworkProtocolVersion.CURRENT
		);
	}

	@WrapOperation(
			method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/PacketByteBuf;readNbt()Lnet/minecraft/nbt/NbtCompound;"
			)
	)
	private static NbtCompound carpetProtocolCompatibilityFix_fixIncomingNbtRead(PacketByteBuf buf, Operation<NbtCompound> original)
	{
		return NetworkUtil.readNbt(buf);
	}

	// no need for rewriting the S->C packet
	// cuz if the HI packet is already sent, before the server knows what client's carpet version is
	// If the client can handle HI, then everything is ok
	// If the client cannot handle HI, then the client will simply disconnect, before anything can be done :(
}
