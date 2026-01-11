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

package carpettisaddition.commands.info.server;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.info.InfoSubcommand;
import carpettisaddition.mixins.command.info.server.EntityAccessor;
import carpettisaddition.utils.Messenger;
import net.minecraft.commands.CommandSourceStack;

import static net.minecraft.commands.Commands.literal;

public class InfoServerCommand extends InfoSubcommand
{
	private static final InfoServerCommand INSTANCE = new InfoServerCommand();

	private InfoServerCommand()
	{
		super("server");
	}

	public static InfoServerCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		context.node.
				then(literal("entity_id_counter").
						executes((c) -> showEntityIdCounter(c.getSource()))
				).
				then(literal("server_tick_counter").
						executes((c) -> showServerTickCounter(c.getSource()))
				);
	}

	private static String computeToIntMaxPercent(int value)
	{
		return String.format("%.4f%%", 100.0 * value / Integer.MAX_VALUE);
	}

	private int showEntityIdCounter(CommandSourceStack source)
	{
		int value = EntityAccessor.getEntityIdCounter$TISCM().get();
		Messenger.tell(source, tr(
				"entity_id_counter",
				Messenger.hover(Messenger.s(value), Messenger.s(computeToIntMaxPercent(value)))
		));
		return 0;
	}

	private int showServerTickCounter(CommandSourceStack source)
	{
		int value = source.getServer().getTickCount();
		Messenger.tell(source, tr(
				"server_tick_counter",
				Messenger.hover(Messenger.s(value), Messenger.s(computeToIntMaxPercent(value)))
		));
		return 0;
	}
}
