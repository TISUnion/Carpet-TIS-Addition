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

import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC >= 11600
//$$ import net.minecraft.text.TextColor;
//#else
import net.minecraft.util.Formatting;
//#endif

@Mixin(Style.class)
public interface StyleAccessor
{
	@Accessor("bold")
	Boolean getBold$TISCM();

	@Accessor("italic")
	Boolean getItalic$TISCM();

	@Accessor("underline")
	Boolean getUnderline$TISCM();

	@Accessor("strikethrough")
	Boolean getStrikethrough$TISCM();

	@Accessor("obfuscated")
	Boolean getObfuscated$TISCM();

	@Accessor("color")
	//#if MC >= 11600
	//$$ TextColor getColor$TISCM();
	//#else
	//#disable-remap
	Formatting getColor$TISCM();
	//#enable-remap
	//#endif
}
