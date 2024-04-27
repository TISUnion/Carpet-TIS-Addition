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

package carpettisaddition.mixins.translations;

import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11600
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mutable;
//#else
//$$ import net.minecraft.util.Formatting;
//#endif

@Mixin(Style.class)
public interface StyleAccessor
{
	@Accessor("bold")
	Boolean getBoldField();

	@Accessor("italic")
	Boolean getItalicField();

	@Accessor("underlined")
	Boolean getUnderlineField();

	@Accessor("strikethrough")
	Boolean getStrikethroughField();

	@Accessor("obfuscated")
	Boolean getObfuscatedField();

	@Accessor("color")
	//#if MC >= 11600
	TextColor getColorField();
	//#else
	//#disable-remap
	//$$ Formatting getColorField();
	//#enable-remap
	//#endif

	@Accessor("hoverEvent")
	HoverEvent getHoverEventField();

	//#if MC >= 11600
	@Mutable
	@Accessor("underlined")
	void setUnderlinedField(Boolean value);

	@Mutable
	@Accessor("strikethrough")
	void setStrikethroughField(Boolean value);

	@Mutable
	@Accessor("obfuscated")
	void setObfuscatedField(Boolean value);
	//#endif
}
