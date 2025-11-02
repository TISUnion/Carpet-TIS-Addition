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
import carpettisaddition.utils.command.BuilderFactory;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.Arrays;
import java.util.Optional;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.SharedSuggestionProvider.suggest;

//#if MC >= 1.21.11
//$$ import net.minecraft.command.permission.Permission;
//$$ import net.minecraft.command.permission.PermissionCheck;
//$$ import net.minecraft.command.permission.PermissionLevel;
//$$ import net.minecraft.commands.Commands;
//#endif

public class CommandUtils
{
	public static boolean isConsoleCommandSource(CommandSourceStack commandSource)
	{
		if (commandSource != null)
		{
			CommandSource output = ((ServerCommandSourceAccessor)commandSource).getOutput();
			return output instanceof MinecraftServer;
		}
		return false;
	}

	public static Optional<ServerPlayer> getPlayer(CommandSourceStack source)
	{
		if (source != null)
		{
			try
			{
				return Optional.ofNullable(source.getPlayerOrException());
			}
			catch (CommandSyntaxException ignored)
			{
			}
		}
		return Optional.empty();
	}

	public static boolean isPlayerCommandSource(CommandSourceStack source)
	{
		return getPlayer(source).isPresent();
	}

	public static boolean isCreativePlayer(CommandSourceStack source)
	{
		return getPlayer(source).
				map(ServerPlayer::isCreative).
				orElse(false);
	}

	public static boolean isOperatorPlayer(CommandSourceStack source)
	{
		return getPlayer(source).
				map(player -> PlayerUtils.isOperator(source.getServer(), player)).
				orElse(false);
	}

	public static boolean isSinglePlayerOwner(CommandSourceStack source)
	{
		MinecraftServer server = source.getServer();
		//#if MC >= 1.16.0
		//$$ return getPlayer(source).
		//$$ 		//#if MC >= 1.21.9
		//$$ 		//$$ map(player -> server.isHost(player.getPlayerConfigEntry())).
		//$$ 		//#else
		//$$ 		map(player -> server.isSingleplayerOwner(player.getGameProfile())).
		//$$ 		//#endif
		//$$ 		orElse(false);
		//#else
		return server.isSingleplayer() && getPlayer(source).
				map(player -> player.getName().getString().equals(server.getSingleplayerName())).
				orElse(false);
		//#endif
	}

	/**
	 * aka source has permission level 2 (commonly used in cheaty commands)
	 */
	public static boolean canCheat(CommandSourceStack source)
	{
		return hasPermissionLevel(source, 2);
	}

	public static boolean hasPermissionLevel(CommandSourceStack source, int level)
	{
		//#if MC >= 1.21.11
		//$$ var permission = new Permission.Level(PermissionLevel.fromLevel(level));
		//$$ return new PermissionCheck.Require(permission).allows(source.getPermissions());
		//#else
		return source.hasPermission(level);
		//#endif
	}

	@FunctionalInterface
	public interface ArgumentGetter<T>
	{
		T get() throws CommandSyntaxException;
	}

	/**
	 * Suppressed the {@link IllegalArgumentException} in {@link com.mojang.brigadier.context.CommandContext#getArgument}
	 * But {@link CommandSyntaxException} is allowed
	 */
	public static <T> Optional<T> getOptArg(ArgumentGetter<T> argGetter) throws CommandSyntaxException
	{
		try
		{
			return Optional.of(argGetter.get());
		}
		catch (IllegalArgumentException e)
		{
			return Optional.empty();
		}
	}

	public static RequiredArgumentBuilder<CommandSourceStack, String> enumArg(String name, Class<? extends Enum<?>> enumClass)
	{
		return argument(name, word()).
				suggests((c, b) -> suggest(
						Arrays.stream(enumClass.getEnumConstants()).
								map(e -> e.name().toLowerCase()),
						b)
				);
	}

	public static BuilderFactory<RequiredArgumentBuilder<CommandSourceStack, String>> enumArg(Class<? extends Enum<?>> enumClass)
	{
		return name -> enumArg(name, enumClass);
	}

	/**
	 * @return the corresponding enum, or Optional.empty() on invalid enum
	 * @throws IllegalArgumentException on arg not found
	 */
	public static <T extends Enum<T>> Optional<T> getEnum(CommandContext<?> context, String argName, Class<T> enumClass)
	{
		String enumString = getString(context, argName);
		try
		{
			return Optional.of(Enum.valueOf(enumClass, enumString.toUpperCase()));
		}
		catch (IllegalArgumentException e)
		{
			return Optional.empty();
		}
	}
}
