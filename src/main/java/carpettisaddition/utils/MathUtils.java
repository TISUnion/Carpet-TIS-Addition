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

public class MathUtils
{
	public static int clamp(int value, int min, int max)
	{
		//#if MC >= 26.4
		//$$ return Math.clamp(value, min, max);
		//#else
		return Math.max(min, Math.min(max, value));
		//#endif
	}

	public static long clamp(long value, long min, long max)
	{
		//#if MC >= 26.4
		//$$ return Math.clamp(value, min, max);
		//#else
		return Math.max(min, Math.min(max, value));
		//#endif
	}

	public static float clamp(float value, float min, float max)
	{
		//#if MC >= 26.4
		//$$ return Math.clamp(value, min, max);
		//#else
		return Math.max(min, Math.min(max, value));
		//#endif
	}

	public static double clamp(double value, double min, double max)
	{
		//#if MC >= 26.4
		//$$ return Math.clamp(value, min, max);
		//#else
		return Math.max(min, Math.min(max, value));
		//#endif
	}
}
