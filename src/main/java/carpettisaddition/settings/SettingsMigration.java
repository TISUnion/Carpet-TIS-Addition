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

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class SettingsMigration
{
	private static final Map<String, String> RULE_RENAMES = ImmutableMap.<String, String>builder()
			.put("natualSpawningUse13Heightmap", "naturalSpawningUse13Heightmap")  // v1.65.0
			.build();

	public static void migrate(String[] fields)
	{
		if (fields.length < 2)
		{
			return;
		}

		String ruleName = fields[0];
		String value = fields[1];
		if (ruleName == null || value == null)
		{
			return;
		}

		if (RULE_RENAMES.containsKey(ruleName))
		{
			ruleName = RULE_RENAMES.get(ruleName);
		}

		fields[0] = ruleName;
		fields[1] = value;
	}
}
