/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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
