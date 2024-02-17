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

package carpettisaddition.mixins.rule.yeetOutOfOrderChatKick;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.network.message.MessageVerifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MessageVerifier.Impl.class)
public abstract class MessageVerifierImplMixin
{
	@ModifyExpressionValue(
			method = "verifyPrecedingSignature",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/network/message/MessageLink;linksTo(Lnet/minecraft/network/message/MessageLink;)Z"
			)
	)
	private boolean yeetOutOfOrderChatKick_impl(boolean linksTo)
	{
		if (CarpetTISAdditionSettings.yeetOutOfOrderChatKick)
		{
			linksTo = true;
		}
		return linksTo;
	}
}
