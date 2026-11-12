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

package carpettisaddition.utils.command;

import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.CommandUtils;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandSourceStack;

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.minecraft.commands.SharedSuggestionProvider.suggest;

//#if MC >= 26.4
//$$ import carpet.helpers.CommandSourceRequirement;
//$$ import carpet.utils.CommandHelper;
//$$ import net.minecraft.commands.Commands;
//$$ import net.minecraft.commands.suggestions.SuggestionProviders;
//$$ import net.minecraft.server.permissions.Permission;
//$$ import net.minecraft.server.permissions.PermissionCheck;
//$$ import net.minecraft.server.permissions.PermissionLevel;
//#endif

public class CommandNodeUtils
{
	public static Predicate<CommandSourceStack> commandPermission(Supplier<?> rule)
	{
		//#if MC >= 26.4
		//$$ return CommandHelper.commandPermission(rule);
		//#else
		return source -> CarpetModUtil.canUseCommand(source, rule.get());
		//#endif
	}

	public static Predicate<CommandSourceStack> requireCondition(BooleanSupplier condition)
	{
		//#if MC >= 26.4
		//$$ return CommandHelper.requireCondition(condition);
		//#else
		return source -> condition.getAsBoolean();
		//#endif
	}

	public static Predicate<CommandSourceStack> requireLevel(int level)
	{
		//#if MC >= 26.4
		//$$ return Commands.hasPermission(new PermissionCheck.Require(new Permission.HasCommandLevel(PermissionLevel.byId(level))));
		//#else
		return source -> CommandUtils.hasPermissionLevel(source, level);
		//#endif
	}

	public static Predicate<CommandSourceStack> requireSource(Predicate<CommandSourceStack> requirement)
	{
		//#if MC >= 26.4
		//$$ return new CommandSourceRequirement<>(requirement);
		//#else
		return requirement;
		//#endif
	}

	public static SuggestionProvider<CommandSourceStack> suggestStrings(Supplier<Stream<String>> values)
	{
		//#if MC >= 26.4
		//$$ return SuggestionProviders.serverOnly((server, context, builder) -> suggest(values.get(), builder));
		//#else
		return (context, builder) -> suggest(values.get(), builder);
		//#endif
	}
}
