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

package carpettisaddition.commands.manipulate;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.block.BlockManipulator;
import carpettisaddition.commands.manipulate.container.ContainerManipulator;
import carpettisaddition.commands.manipulate.entity.EntityManipulator;
import carpettisaddition.utils.CarpetModUtil;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class ManipulateCommand extends AbstractCommand
{
	private static final String NAME = "manipulate";
	private static final ManipulateCommand INSTANCE = new ManipulateCommand();

	private static final List<AbstractManipulator> MANIPULATORS = ImmutableList.of(
			new BlockManipulator(),
			ContainerManipulator.getInstance(),
			new EntityManipulator()
	);

	public ManipulateCommand()
	{
		super(NAME);
	}

	public static ManipulateCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		LiteralArgumentBuilder<ServerCommandSource> root = literal(NAME).
			requires(player -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandManipulate));

		MANIPULATORS.forEach(m -> {
			LiteralArgumentBuilder<ServerCommandSource> child = literal(m.getName());
			m.buildSubCommand(context.node(child));
			root.then(child);
		});

		context.dispatcher.register(root);
	}
}
