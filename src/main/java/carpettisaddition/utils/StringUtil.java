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

package carpettisaddition.utils;

import java.text.NumberFormat;

public class StringUtil
{
	public static String removePrefix(String string, String... prefixes)
	{
		for (String prefix : prefixes)
		{
			if (string.startsWith(prefix))
			{
				string = string.substring(prefix.length());
			}
		}
		return string;
	}

	public static String removeSuffix(String string, String... suffixes)
	{
		for (String suffix : suffixes)
		{
			if (string.endsWith(suffix))
			{
				string = string.substring(0, string.length() - suffix.length());
			}
		}
		return string;
	}

	public static String fractionDigit(double value, int digit)
	{
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumFractionDigits(digit);
		nf.setMaximumFractionDigits(digit);
		return nf.format(value);
	}
}
