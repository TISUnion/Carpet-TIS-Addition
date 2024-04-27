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

package carpettisaddition.commands.removeentity;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.command.argument.EntityArgumentType.entities;
import static net.minecraft.command.argument.EntityArgumentType.getEntities;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RemoveEntityCommand extends AbstractCommand
{
	private static final String NAME = "removeentity";
	private static final RemoveEntityCommand INSTANCE = new RemoveEntityCommand();

	private RemoveEntityCommand()
	{
		super(NAME);
	}

	public static RemoveEntityCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		LiteralArgumentBuilder<ServerCommandSource> node = literal(NAME).
				requires(
						player -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandRemoveEntity)
				).
				then(argument("target", entities()).
						executes(c -> removeEntities(c.getSource(), getEntities(c, "target")))
				);

		context.dispatcher.register(node);
	}

	private int removeEntities(ServerCommandSource source, Collection<? extends Entity> entities)
	{
		List<? extends Entity> nonPlayerEntities = entities.stream().
				filter(entity -> !(entity instanceof PlayerEntity)).
				collect(Collectors.toList());
		nonPlayerEntities.forEach(entity -> {
			if (entity instanceof EntityToBeCleanlyRemoved)
			{
				((EntityToBeCleanlyRemoved)entity).setToBeCleanlyRemoved$TISCM();
			}
			//#if MC >= 11700
			entity.discard();
			//#else
			//$$ entity.remove();
			//#endif
		});
		Messenger.tell(source, tr("success", nonPlayerEntities.size()), true);
		return nonPlayerEntities.size();
	}
}
