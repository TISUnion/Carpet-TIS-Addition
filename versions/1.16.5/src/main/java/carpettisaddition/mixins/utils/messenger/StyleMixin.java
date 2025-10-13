/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.utils.messenger;

import carpettisaddition.utils.StyleExt;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

// impl in mc1.16 only
@Mixin(Style.class)
public abstract class StyleMixin implements StyleExt
{
	@Shadow @Final private @Nullable TextColor color;
	@Shadow @Final @Mutable private @Nullable Boolean underlined;
	@Shadow @Final @Mutable private @Nullable Boolean strikethrough;
	@Shadow @Final @Mutable private @Nullable Boolean obfuscated;

	@Shadow
	public abstract Style withColor(@Nullable TextColor color);

	@Override
	public Style withUnderline$TISCM(Boolean underlined)
	{
		Style copy = this.withColor(this.color);
		((StyleMixin)(Object)copy).underlined = underlined;
		return copy;
	}

	@Override
	public Style withObfuscated$TISCM(Boolean obfuscated)
	{
		Style copy = this.withColor(this.color);
		((StyleMixin)(Object)copy).obfuscated = obfuscated;
		return copy;
	}

	@Override
	public Style withStrikethrough$TISCM(Boolean strikethrough)
	{
		Style copy = this.withColor(this.color);
		((StyleMixin)(Object)copy).strikethrough = strikethrough;
		return copy;
	}
}
