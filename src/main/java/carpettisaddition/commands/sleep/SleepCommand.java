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

package carpettisaddition.commands.sleep;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

import java.util.Arrays;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class SleepCommand extends AbstractCommand
{
	private static final String NAME = "sleep";
	private static final SleepCommand INSTANCE = new SleepCommand();

	private SleepCommand()
	{
		super(NAME);
	}

	public static SleepCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		RequiredArgumentBuilder<ServerCommandSource, Integer> durationNode = argument("duration", integer(0, 60_000));
		for (TimeUnit timeUnit : TimeUnit.values())
		{
			durationNode.then(literal(timeUnit.name).
					executes(c -> doLag(c.getSource(), timeUnit, getInteger(c, "duration")))
			);
		}

		context.dispatcher.register(
				literal(NAME).
				requires(s -> CarpetModUtil.canUseCommand(s, CarpetTISAdditionSettings.commandSleep)).
				executes(c -> showHelp(c.getSource())).
				then(durationNode)
		);
	}

	private int showHelp(ServerCommandSource source)
	{
		BaseText usage = Messenger.c(
				"w /" + NAME,
				"g  <", "w duration", "g > (",
				Messenger.join(
						Messenger.s("|", Formatting.GRAY),
						Arrays.stream(TimeUnit.values()).
								map(tu -> Messenger.s(tu.name)).
								toArray(BaseText[]::new)
				),
				"g )"
		);
		Messenger.tell(source, tr("help"));
		Messenger.tell(source, tr("usage", usage));
		Messenger.tell(source, Messenger.c(
				Messenger.formatting(tr("warning_header"), Formatting.BOLD),
				Messenger.s(": "),
				tr("warning_content")
		));
		return 0;
	}

	private int doLag(ServerCommandSource source, TimeUnit timeUnit, int duration)
	{
		try
		{
			timeUnit.action.sleep(duration);
		}
		catch (InterruptedException e)
		{
			throw new RuntimeException(e);
		}
		return 0;
	}

	private enum TimeUnit
	{
		SECOND("s", duration -> Thread.sleep(duration * 1000L)),
		MILLI_SECOND("ms", duration -> Thread.sleep(duration)),
		MICRO_SECOND("us", duration -> Thread.sleep(duration / 1000, (int)(duration % 1000)));

		public final String name;
		public final SleepAction action;

		TimeUnit(String name, SleepAction action)
		{
			this.name = name;
			this.action = action;
		}

		@FunctionalInterface
		interface SleepAction
		{
			void sleep(long duration) throws InterruptedException;
		}
	}
}
