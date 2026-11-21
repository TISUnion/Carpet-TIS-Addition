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

package carpettisaddition.helpers.rule.redstoneDustRandomUpdateOrder;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public enum RandomizerType
{
	NONE(false, Arrays.asList("false", "none")),
	RANDOM_SHUFFLE(true, Arrays.asList("true", "shuffle")),
	RANDOM_POSITION(true, Arrays.asList("random_pos"));

	private final boolean willModify;
	private final List<String> keywords;

	RandomizerType(boolean willModify, List<String> keywords)
	{
		this.willModify = willModify;
		this.keywords = keywords;
	}

	public static Optional<RandomizerType> fromString(String value)
	{
		value = value.toLowerCase(Locale.ROOT);
		for (RandomizerType randomizerType : values())
		{
			if (randomizerType.keywords.contains(value))
			{
				return Optional.of(randomizerType);
			}
		}
		return Optional.empty();
	}

	public boolean willModifyUpdateOrder()
	{
		return this.willModify;
	}
}
