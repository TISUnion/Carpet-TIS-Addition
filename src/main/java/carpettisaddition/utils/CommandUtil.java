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
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.Optional;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandSource.suggestMatching;

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

	public static Optional<ServerPlayerEntity> getPlayer(ServerCommandSource source)
	{
		if (source != null)
		{
			try
			{
				return Optional.ofNullable(source.getPlayer());
			}
			catch (CommandSyntaxException ignored)
			{
			}
		}
		return Optional.empty();
	}

	public static boolean isPlayerCommandSource(ServerCommandSource source)
	{
		return getPlayer(source).isPresent();
	}

	public static boolean isCreativePlayer(ServerCommandSource source)
	{
		return getPlayer(source).
				map(ServerPlayerEntity::isCreative).
				orElse(false);
	}

	public static boolean isOperatorPlayer(ServerCommandSource source)
	{
		return getPlayer(source).
				map(player -> source.getMinecraftServer().getPlayerManager().isOperator(player.getGameProfile())).
				orElse(false);
	}

	public static boolean canCheat(ServerCommandSource source)
	{
		return source.hasPermissionLevel(2);  // commonly used in cheaty commands
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

	public static RequiredArgumentBuilder<ServerCommandSource, String> enumArg(String name, Class<? extends Enum<?>> enumClass)
	{
		return argument(name, word()).
				suggests((c, b) -> suggestMatching(
						Arrays.stream(enumClass.getEnumConstants()).
								map(e -> e.name().toLowerCase()),
						b)
				);
	}

	public static BuilderFactory<RequiredArgumentBuilder<ServerCommandSource, String>> enumArg(Class<? extends Enum<?>> enumClass)
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
