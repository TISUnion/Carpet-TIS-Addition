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

package carpettisaddition.helpers.rule.updateSuppressionSimulator;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;

public class UpdateSuppressionSimulator
{
	private static final Map<String, Runnable> SUPPORTED_ERRORS = ImmutableMap.of(
			"StackOverflowError", () -> {
				throw new StackOverflowError("TISCM UpdateSuppressionSimulator");
			},
			"OutOfMemoryError", () -> {
				throw new OutOfMemoryError("TISCM UpdateSuppressionSimulator");
			},
			"ClassCastException", () -> {
				throw new ClassCastException("TISCM UpdateSuppressionSimulator");
			}
	);

	private static final Runnable DUMMY = () -> {};
	private static Runnable nuke = DUMMY;

	public static boolean isActivated()
	{
		return nuke != DUMMY;
	}

	public static void kaboom()
	{
		nuke.run();
	}

	private static Optional<Runnable> parseRule(String ruleValue)
	{
		switch (ruleValue)
		{
			case "true":
				return Optional.of(SUPPORTED_ERRORS.values().iterator().next());
			case "false":
				return Optional.of(DUMMY);
			default:
				Runnable[] ret = new Runnable[]{null};
				SUPPORTED_ERRORS.forEach((key, value) -> {
					if (key.equalsIgnoreCase(ruleValue))
					{
						ret[0] = value;
					}
				});
				return Optional.ofNullable(ret[0]);
		}
	}

	public static void acceptRule(String ruleValue)
	{
		nuke = parseRule(ruleValue).orElseThrow(RuntimeException::new);
	}

	public static boolean checkRule(String ruleValue)
	{
		return parseRule(ruleValue).isPresent();
	}
}
