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

package carpettisaddition.mixins.carpet.tweaks.messenger;

import carpet.utils.Messenger;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"ConstantConditions", "DefaultAnnotationParam"})
@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<1.16"))
@Mixin(Messenger.class)
public abstract class MessengerMixin
{
	// these tweaks are only necessary for old carpet versions

	@Redirect(
			//#if MC >= 11500
			method = "parseStyle",
			//#else
			//$$ method = "_applyStyleToTextComponent",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setItalic(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setItalic(Style style, Boolean value)
	{
		return value ? style.setItalic(value) : style;
	}

	@Redirect(
			//#if MC >= 11500
			method = "parseStyle",
			//#else
			//$$ method = "_applyStyleToTextComponent",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setStrikethrough(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setStrikethrough(Style style, Boolean value)
	{
		return value ? style.setStrikethrough(value) : style;
	}

	@Redirect(
			//#if MC >= 11500
			method = "parseStyle",
			//#else
			//$$ method = "_applyStyleToTextComponent",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setUnderline(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setUnderline(Style style, Boolean value)
	{
		return value ? style.setUnderline(value) : style;
	}

	@Redirect(
			//#if MC >= 11500
			method = "parseStyle",
			//#else
			//$$ method = "_applyStyleToTextComponent",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setBold(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setBold(Style style, Boolean value)
	{
		return value ? style.setBold(value) : style;
	}

	@Redirect(
			//#if MC >= 11500
			method = "parseStyle",
			//#else
			//$$ method = "_applyStyleToTextComponent",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/Style;setObfuscated(Ljava/lang/Boolean;)Lnet/minecraft/text/Style;",
					remap = true
			),
			remap = false
	)
	private static Style doIfCheckPlease_setObfuscated(Style style, Boolean value)
	{
		return value ? style.setObfuscated(value) : style;
	}
}
