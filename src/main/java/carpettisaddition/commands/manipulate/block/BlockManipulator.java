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

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.AbstractManipulator;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;

public class BlockManipulator extends AbstractManipulator
{
	private static final BlockManipulator INSTANCE = new BlockManipulator();
	private final BlockManipulatorImplEmit emitImpl = new BlockManipulatorImplEmit(this.getTranslator());
	private final BlockManipulatorImplExecute executeImpl = new BlockManipulatorImplExecute(this.getTranslator());

	private BlockManipulator()
	{
		super("block");
	}

	public static BlockManipulator getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void buildSubCommand(CommandTreeContext.Node context)
	{
		CommandBuilder builder = new CommandBuilder();

		builder.add(
				"execute", "block_event",
				(node, cmd) ->
						node.then(argument("type", integer()).
								then(argument("data", integer()).
										executes(cmd)
								)
						),
				(c, pos) -> this.executeImpl.executeBlockEvent(
						c.getSource(),
						pos,
						getInteger(c, "type"),
						getInteger(c, "data")
				),
				c -> new Object[]{getInteger(c, "type"), getInteger(c, "data")}
		);
		builder.add("execute", "tile_tick", this.executeImpl::executeTileTickAt);
		builder.add("execute", "random_tick", this.executeImpl::executeRandomTickAt);
		builder.add("execute", "precipitation_tick", this.executeImpl::executePrecipitationTickAt);

		builder.add("emit", "block_update", this.emitImpl::emitBlockUpdateAt);
		builder.add("emit", "state_update", this.emitImpl::emitStateUpdateAt);
		builder.add("emit", "light_update", this.emitImpl::emitLightUpdateAt);

		builder.build(context.node);
	}
}
