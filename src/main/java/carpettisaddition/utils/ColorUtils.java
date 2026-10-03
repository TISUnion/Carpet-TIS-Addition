/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.utils;

import net.minecraft.world.item.DyeColor;

//#if MC >= 26.4
//$$ import net.minecraft.world.level.block.ColorCollection;
//#else
import carpettisaddition.mixins.utils.DyeColorAccessor;
//#endif

public class ColorUtils
{
	//#if MC >= 26.4
	// Minecraft 26.4-snapshot-2: net.minecraft.client.renderer.blockentity.AbstractSignRenderer.BRIGHT_TEXT_COLORS.
	// Keep the palette here so server-side code does not depend on the client renderer.
	//$$ private static final ColorCollection<Integer> TEXT_COLORS = new ColorCollection<>(
	//$$ 		0xFFFFFFFF, 0xFFFF681F, 0xFFFF00FF, 0xFF9AC0CD,
	//$$ 		0xFFFFFF00, 0xFFBFFF00, 0xFFFF69B4, 0xFF808080,
	//$$ 		0xFFD3D3D3, 0xFF00FFFF, 0xFFA020F0, 0xFF0000FF,
	//$$ 		0xFF8B4513, 0xFF00FF00, 0xFFFF0000, 0xFF000000
	//$$ );
	//#endif

	public static int getTextColor(DyeColor dyeColor)
	{
		//#if MC >= 26.4
		//$$ return TEXT_COLORS.pick(dyeColor);
		//#else
		return ((DyeColorAccessor)(Object)dyeColor).getTextColor$TISCM();
		//#endif
	}
}
