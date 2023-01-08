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

import carpettisaddition.mixins.utils.ServerCommandSourceAccessor;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;

public class CommandUtil
{
	public static boolean isConsoleCommandSource(ServerCommandSource commandSource)
	{
		if (commandSource != null)
		{
			CommandOutput output = ((ServerCommandSourceAccessor)commandSource).getOutput();
			return output instanceof MinecraftServer;
		}
		return false;
	}

	public static boolean isPlayerCommandSource(ServerCommandSource commandSource)
	{
		if (commandSource != null)
		{
			try
			{
				commandSource.getPlayer();
				return true;
			}
			catch (CommandSyntaxException e)
			{
				return false;
			}
		}
		return false;
	}
}
