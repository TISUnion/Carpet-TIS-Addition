package carpettisaddition.utils;

import net.minecraft.text.MutableText;

public class CounterUtil
{
	private static double getRatePerHourValue(long rate, long ticks)
	{
		return (double)rate / ticks * (20 * 60 * 60);
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
	public static MutableText ratePerHourText(long rate, long ticks, String fmt)
	{
		assert fmt.length() == 3;
		return Messenger.c(
				fmt.charAt(0) + " " + rate,
				fmt.charAt(1) + " , (",
				String.format("%s %.1f", fmt.charAt(2), getRatePerHourValue(rate, ticks)),
				fmt.charAt(1) + " /h)"
		);
	}

	public static MutableText ratePerHourText(int rate, long ticks, String fmt)
	{
		return ratePerHourText((long)rate, ticks, fmt);
	}
}
