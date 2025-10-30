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

package carpettisaddition.commands.info.entity;

import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.utils.Messenger;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

/**
 * Logic ports from fabric-carpet 1.4.54
 * Newer carpet remove entity info framework, so we need to keep a copy of that
 * Only used in mc1.17+
 */
public class EntityInfoCommand implements CommandExtender
{
	private static final EntityInfoCommand INSTANCE = new EntityInfoCommand();

	public static EntityInfoCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		context.node.then(literal("entity").
				then(argument("entity selector", EntityArgument.entities()).
						executes((c) -> infoEntities(
								c.getSource(), EntityArgument.getEntities(c, "entity selector"), null)
						).
						then(literal("grep").
								then(argument("regexp", greedyString()).
										executes((c) -> infoEntities(
												c.getSource(),
												EntityArgument.getEntities(c, "entity selector"),
												getString(c, "regexp")
										))
								)
						)
				)
		);
	}

	private static int infoEntities(CommandSourceStack source, Collection<? extends Entity> entities, String grep)
	{
		for (Entity e : entities)
		{
			List<BaseComponent> report = EntityInfoPorting.entityInfo(e, source.getLevel());
			printEntity(report, source, grep);
		}
		return 1;
	}

	private static void printEntity(List<BaseComponent> messages, CommandSourceStack source, String grep)
	{
		List<BaseComponent> actual = new ArrayList<>();
		if (grep != null)
		{
			Pattern p = Pattern.compile(grep);
			actual.add(messages.get(0));
			boolean empty = true;
			for (int i = 1; i < messages.size(); i++)
			{
				BaseComponent line = messages.get(i);
				Matcher m = p.matcher(line.getString());
				if (m.find())
				{
					empty = false;
					actual.add(line);
				}
			}
			if (empty)
			{
				return;
			}
		}
		else
		{
			actual = messages;
		}
		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, actual);
	}
}