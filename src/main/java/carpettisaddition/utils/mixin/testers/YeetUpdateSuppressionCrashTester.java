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

package carpettisaddition.utils.mixin.testers;

import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.mixin.ConditionTester;
import me.fallenbreath.conditionalmixin.api.util.VersionChecker;
import net.fabricmc.loader.api.FabricLoader;

public class YeetUpdateSuppressionCrashTester implements ConditionTester
{
	@Override
	public boolean isSatisfied(String mixinClassName)
	{
		return isAllowed();
	}

	public static boolean isAllowed()
	{
		return
				!isMatched(ModIds.carpet, ">=1.4.49 <=1.4.76") &&
				!isMatched(ModIds.carpet_extra, ">=1.4.14 <=1.4.43");
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private static boolean isMatched(String modId, String versionPredicate)
	{
		return FabricLoader.getInstance().
				getModContainer(modId).
				map(mod -> VersionChecker.doesVersionSatisfyPredicate(mod.getMetadata().getVersion(), versionPredicate)).
				orElse(false);
	}
}
