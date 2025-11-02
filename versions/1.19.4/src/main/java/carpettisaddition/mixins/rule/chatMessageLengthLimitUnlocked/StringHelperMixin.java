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
import net.minecraft.util.StringUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(StringUtil.class)
public abstract class StringHelperMixin
{
	@ModifyArg(
			method = "trimChatMessage",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/StringUtil;truncateStringIfNecessary(Ljava/lang/String;IZ)Ljava/lang/String;"
			)
	)
	private static int	chatMessageLengthLimitUnlocked_tweakTruncateChat(int limit)
	{
		if (CarpetTISAdditionSettings.chatMessageLengthLimitUnlocked)
		{
			limit = ChatMessageLengthLimitUnlockedHelper.LIMIT_OVERRIDE;
		}
		return limit;
	}
}
