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

package carpettisaddition.settings;

import carpet.settings.ParsedRule;
import carpettisaddition.CarpetTISAdditionMod;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TISCMRules
{
	private static final Map<String, TISCMRule> RULES_BY_NAME = Maps.newLinkedHashMap();
	private static final Map<ParsedRule<?>, TISCMRule> RULES_BY_CM_RULE = Maps.newLinkedHashMap();

	static void store(List<TISCMRule> rules)
	{
		for (TISCMRule rule : rules)
		{
			RULES_BY_NAME.put(rule.getName(), rule);
			RULES_BY_CM_RULE.put(rule.getCarpetRule(), rule);

			if (rule.worksForCurrentMCVersion() && !rule.allRestrictionsSatisfied())
			{
				String reasons = String.join("; ", rule.getAllRestrictionsFailReasons());
				CarpetTISAdditionMod.LOGGER.warn("Rule {} is disabled by mod restriction: {}", rule.getName(), reasons);
			}
		}
	}

	public static Optional<TISCMRule> get(String ruleName)
	{
		return Optional.ofNullable(RULES_BY_NAME.get(ruleName));
	}

	public static Optional<TISCMRule> get(ParsedRule<?> carpetRule)
	{
		return Optional.ofNullable(RULES_BY_CM_RULE.get(carpetRule));
	}
}
