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

package carpettisaddition.logging.loggers.microtiming.marker;

import net.minecraft.ChatFormatting;

public enum MicroTimingMarkerType
{
	/**
	 * Dont log block update
	 */
	REGULAR(2.5F, ChatFormatting.GRAY),
	/**
	 * Log everything
	 */
	END_ROD(7.0F, ChatFormatting.LIGHT_PURPLE);

	public final float lineWidth;
	private final ChatFormatting color;

	MicroTimingMarkerType(float lineWidth, ChatFormatting color)
	{
		this.lineWidth = lineWidth;
		this.color = color;
	}

	public float getLineWidth()
	{
		return this.lineWidth;
	}

	public String getFancyString()
	{
		return this.color.toString() + super.toString() + ChatFormatting.RESET;
	}
}
