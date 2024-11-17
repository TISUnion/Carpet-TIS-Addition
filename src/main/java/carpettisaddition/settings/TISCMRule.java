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
import carpettisaddition.utils.ModIds;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.fallenbreath.conditionalmixin.api.util.VersionChecker;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TISCMRule
{
	private final Field field;
	private final ParsedRule<?> carpetRule;
	private final ParsedRestrictions allRestrictions;
	private final ParsedRestrictions mcRestrictions;

	private static class ParsedRestrictions
	{
		public final boolean satisfied;
		public final List<String> badReasons;

		public ParsedRestrictions(Restriction[] restrictions, Predicate<Condition> filter)
		{
			List<String> badReasons = Lists.newArrayList();
			boolean ok = restrictions.length == 0;
			for (Restriction r : restrictions)
			{
				List<Condition> badR = Arrays.stream(r.require()).
						filter(filter).
						filter(c -> !checkCondition(c)).
						collect(Collectors.toList());
				List<Condition> badC = Arrays.stream(r.conflict()).
						filter(filter).
						filter(ParsedRestrictions::checkCondition).
						collect(Collectors.toList());
				ok |= badR.isEmpty() && badC.isEmpty();
				if (!badR.isEmpty())
				{
					String s = badR.stream().map(ParsedRestrictions::formatCondition).collect(Collectors.joining(", "));
					badReasons.add("requirement " + s + " unmet");
				}
				if (!badC.isEmpty())
				{
					String s = badC.stream().map(ParsedRestrictions::formatCondition).collect(Collectors.joining(", "));
					badReasons.add("conflict " + s + " found");
				}
			}
			this.satisfied = ok;
			this.badReasons = ImmutableList.copyOf(badReasons);
		}

		private static boolean checkCondition(Condition c)
		{
			if (c.type() != Condition.Type.MOD)
			{
				throw new IllegalArgumentException("Only conditions of type MOD are supported");
			}
			return VersionChecker.doesModVersionSatisfyPredicate(c.value(), Arrays.asList(c.versionPredicates()));
		}

		private static String formatCondition(Condition c)
		{
			String s = c.value();
			if (c.versionPredicates().length > 0)
			{
				s += " " + String.join("||", c.versionPredicates());
			}
			return "'" + s + "'";
		}
	}

	public TISCMRule(Rule annotation, Field field, ParsedRule<?> carpetRule)
	{
		this.field = field;
		this.carpetRule = carpetRule;
		this.allRestrictions = new ParsedRestrictions(annotation.restrictions(), c -> true);
		this.mcRestrictions = new ParsedRestrictions(annotation.restrictions(), c -> c.value().equals(ModIds.minecraft));
	}

	public ParsedRule<?> getCarpetRule()
	{
		return this.carpetRule;
	}

	public String getName()
	{
		return this.field.getName();
	}

	public boolean allRestrictionsSatisfied()
	{
		return this.allRestrictions.satisfied;
	}

	public List<String> getAllRestrictionsFailReasons()
	{
		return this.allRestrictions.badReasons;
	}

	public boolean worksForCurrentMCVersion()
	{
		return this.mcRestrictions.satisfied;
	}

	public List<String> getMCVersionFailReasons()
	{
		return this.allRestrictions.badReasons;
	}
}
