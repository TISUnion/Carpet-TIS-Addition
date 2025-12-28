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

import net.minecraft.commands.CommandSourceStack;

//#if MC >= 11700
import carpet.CarpetSettings;
//#endif

public class CarpetModUtil
{
	/**
	 * Copied from {@link carpet.settings.SettingsManager#canUseCommand} in fabric-carpet 1.4.19
	 * In fabric-carpet 1.4.19 the signature of method canUseCommand is changed, so I made a copy version of the method
	 * for backwards compatibility for Minecraft 1.16.4 and some 1.17 snapshots
	 * Firstly try to use carpet's method, if failed, use our own copied implementation
	 */
	public static boolean canUseCommand(CommandSourceStack source, Object commandLevel)
	{
		//#if MC >= 11901
		//$$ return carpet.utils.CommandHelper.canUseCommand(source, commandLevel);
		//#elseif MC >= 11600
		//$$ return carpet.settings.SettingsManager.canUseCommand(source, commandLevel);
		//#else
		if (commandLevel instanceof Boolean) return (Boolean) commandLevel;
		String commandLevelString = commandLevel.toString();
		switch (commandLevelString)
		{
			case "true": return true;
			case "false": return false;
			case "ops": return source.hasPermission(2); // typical for other cheaty commands
			case "0":
			case "1":
			case "2":
			case "3":
			case "4":
				return source.hasPermission(Integer.parseInt(commandLevelString));
		}
		return false;
		//#endif
	}

	/**
	 * @return if {@link #canUseCommand} will always return false, i.e. command completely disabled
	 */
	public static boolean isCanUseCommandAlwaysFalse(Object commandLevel)
	{
		if (commandLevel instanceof Boolean) return !((Boolean)commandLevel);
		return commandLevel.toString().equals("false");
	}

	// alias of canUseCommand
	public static boolean hasEnoughPermission(CommandSourceStack source, String permissionRuleValue)
	{
		return canUseCommand(source, permissionRuleValue);
	}

	public static boolean canUseCarpetCommand(CommandSourceStack source)
	{
		// rule carpetCommandPermissionLevel is added in fabric carpet v1.4.55
		Object level =
				//#if MC >= 11700
				//$$ CarpetSettings.carpetCommandPermissionLevel;
				//#else
				2;
				//#endif

		return canUseCommand(source, level);
	}
}
