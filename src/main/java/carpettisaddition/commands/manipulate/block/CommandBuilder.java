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

package carpettisaddition.commands.manipulate.block;

import com.google.common.collect.Maps;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.function.BiConsumer;

import static net.minecraft.command.arguments.BlockPosArgumentType.blockPos;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

class CommandBuilder
{
	private final ArgumentBuilder<ServerCommandSource, ?> rangedRoot;
	private final Map<String, ArgumentBuilder<ServerCommandSource, ?>> actionNodes = Maps.newHashMap();

	@FunctionalInterface
	public interface LeafBuilder
	{
		ArgumentBuilder<ServerCommandSource, ?> build(ArgumentBuilder<ServerCommandSource, ?> node, Command<ServerCommandSource> cmd);
	}

	public CommandBuilder()
	{
		this.rangedRoot = argument("to", blockPos());
	}

	public void build(ArgumentBuilder<ServerCommandSource, ?> node)
	{
		this.actionNodes.values().forEach(this.rangedRoot::then);
		node.then(argument("from", blockPos()).then(this.rangedRoot));
	}

	public void add(
			String action, String name,
			LeafBuilder leafBuilder,
			BlockExecutor.ExecuteImpl impl,
			BlockExecutor.MessageExtraArgsGetter messageExtraArgsGetter
	)
	{
		BlockExecutor executor = new BlockExecutor(impl, action + "." + name, messageExtraArgsGetter);
		this.actionNodes.
				computeIfAbsent(action, k -> literal(action)).
				then(leafBuilder.build(literal(name), executor::process));
	}

	public void add(String action, String name, BiConsumer<ServerCommandSource, BlockPos> impl)
	{
		this.add(
				action, name,
				ArgumentBuilder::executes,
				(ctx, pos) -> impl.accept(ctx.getSource(), pos),
				BlockExecutor.MessageExtraArgsGetter.NONE
		);
	}
}
