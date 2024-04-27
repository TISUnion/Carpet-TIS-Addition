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

package carpettisaddition.mixins.rule.chatMessageLengthLimitUnlocked;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.chatMessageLengthLimitUnlocked.ChatMessageLengthLimitUnlockedHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatMessageC2SPacket.class)
public abstract class ChatMessageC2SPacketMixin
{
	//#if MC >= 11900
	//$$ @ModifyArg(
	//$$ 		method = "write",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/network/PacketByteBuf;writeString(Ljava/lang/String;I)Lnet/minecraft/network/PacketByteBuf;"
	//$$ 		)
	//$$ )
	//#else
	@ModifyExpressionValue(
			method = "<init>(Ljava/lang/String;)V",
			at = @At(
					value = "CONSTANT",
					args = "intValue=256"
			),
			require = 2
	)
	//#endif
	private int chatMessageLengthLimitUnlocked_tweakChatPacketWrite(int limit)
	{
		if (CarpetTISAdditionSettings.chatMessageLengthLimitUnlocked)
		{
			limit = ChatMessageLengthLimitUnlockedHelper.LIMIT_OVERRIDE;
		}
		return limit;
	}

	@ModifyArg(
			//#if MC >= 11700
			method = "<init>(Lnet/minecraft/network/PacketByteBuf;)V",
			//#else
			//$$ method = "read",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/PacketByteBuf;readString(I)Ljava/lang/String;"
			)
	)
	private
	//#if MC >= 11900
	//$$ static
	//#endif
	int	chatMessageLengthLimitUnlocked_tweakChatPacketRead(int limit)
	{
		if (CarpetTISAdditionSettings.chatMessageLengthLimitUnlocked)
		{
			limit = ChatMessageLengthLimitUnlockedHelper.LIMIT_OVERRIDE;
		}
		return limit;
	}
}
