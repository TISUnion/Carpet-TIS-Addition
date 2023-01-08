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

import net.minecraft.text.BaseText;

public class CounterUtil
{
	private static final int MS_PER_TICK = 50;

	public static long getTimeElapsed(long startTick, long startMillis, boolean realtime)
	{
		return Math.max(1, realtime ? (System.currentTimeMillis() - startMillis) / MS_PER_TICK : GameUtil.getGameTime() - startTick);
	}

	public static double tickToMinute(long ticks)
	{
		return ticks / (20.0 * 60.0);
	}

	public static double tickToHour(long ticks)
	{
		return ticks / (20.0 * 60.0 * 60.0);
	}

	private static double getRatePerHourValue(long rate, long ticks)
	{
		return (double)rate / tickToHour(ticks);
	}

	public static String ratePerHour(long rate, long ticks)
	{
		return String.format("%d, (%.1f/h)", rate, getRatePerHourValue(rate, ticks));
	}

	public static String ratePerHour(int rate, long ticks)
	{
		return ratePerHour((long)rate, ticks);
	}

	/**
	 * @param fmt a carpet color formatting string with 3 chars, for example "wgg"
	 *            for a result of "%d, (%.1f/h)"
	 *            "%d" uses fmt[0]
	 *            ",", "(", "/h)" uses fmt[1]
	 *            "%.1f uses fmt[2]
 	 */
	public static BaseText ratePerHourText(long rate, long ticks, String fmt)
	{
		assert fmt.length() == 3;
		return Messenger.c(
				fmt.charAt(0) + " " + rate,
				fmt.charAt(1) + " , (",
				String.format("%s %.1f", fmt.charAt(2), getRatePerHourValue(rate, ticks)),
				fmt.charAt(1) + " /h)"
		);
	}

	public static BaseText ratePerHourText(int rate, long ticks, String fmt)
	{
		return ratePerHourText((long)rate, ticks, fmt);
	}
}
