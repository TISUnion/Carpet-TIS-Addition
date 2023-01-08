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

package carpettisaddition.commands.spawn.mobcapsLocal;

import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.logging.loggers.mobcapsLocal.MobcapsLocalLogger;
import carpettisaddition.mixins.command.mobcapsLocal.SpawnCommandAccessor;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class MobcapsLocalCommand extends AbstractCommand implements CommandExtender
{
	private static final MobcapsLocalCommand INSTANCE = new MobcapsLocalCommand();

	private MobcapsLocalCommand()
	{
		super("spawn.mobcapsLocal");
	}

	public static MobcapsLocalCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		context.node.then(literal("mobcapsLocal").
				executes(c -> showLocalMobcaps(c.getSource(), c.getSource().getPlayer())).
				then(argument("player", EntityArgumentType.player()).
						executes(c -> showLocalMobcaps(c.getSource(), getPlayer(c, "player")))
				)
		);
	}

	private int showLocalMobcaps(ServerCommandSource source, ServerPlayerEntity targetPlayer) throws CommandSyntaxException
	{
		int[] ret = new int[1];
		MobcapsLocalLogger.getInstance().withLocalMobcapContext(
				targetPlayer,
				() -> {
					Messenger.tell(source, tr("info", targetPlayer.getDisplayName()));
					ret[0] = SpawnCommandAccessor.invokeGeneralMobcaps(source);
				},
				() -> ret[0] = 0
		);
		return ret[0];
	}
}