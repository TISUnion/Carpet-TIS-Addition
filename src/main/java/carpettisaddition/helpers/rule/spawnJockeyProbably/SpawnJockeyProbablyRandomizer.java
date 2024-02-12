package carpettisaddition.helpers.rule.spawnJockeyProbably;

import carpettisaddition.CarpetTISAdditionSettings;

import java.util.Random;

public class SpawnJockeyProbablyRandomizer
{
	public static final Random RANDOM = new Random();

	public static boolean isEnabled()
	{
		return CarpetTISAdditionSettings.spawnJockeyProbably >= 0;
	}

	public static boolean shouldSpawnJockey()
	{
		double p = CarpetTISAdditionSettings.spawnJockeyProbably;
		return p > 0 && RANDOM.nextFloat() <= p;
	}

	public static <T> T tweak(T originalValue, T jockeyValue, T notJockeyValue)
	{
		if (isEnabled())
		{
			return shouldSpawnJockey() ? jockeyValue : notJockeyValue;
		}
		return originalValue;
	}
}
