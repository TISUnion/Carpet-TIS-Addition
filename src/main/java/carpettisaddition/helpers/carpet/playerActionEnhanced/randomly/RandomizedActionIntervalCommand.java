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

package carpettisaddition.helpers.carpet.playerActionEnhanced.randomly;

import carpet.helpers.EntityPlayerActionPack;
import carpettisaddition.helpers.carpet.playerActionEnhanced.IEntityPlayerActionPackAction;
import carpettisaddition.helpers.carpet.playerActionEnhanced.randomly.gen.*;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

import java.util.Optional;
import java.util.function.Function;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RandomizedActionIntervalCommand extends TranslationContext
{
	private static final RandomizedActionIntervalCommand INSTANCE = new RandomizedActionIntervalCommand();

	private RandomizedActionIntervalCommand()
	{
		super("command.TODO");
	}

	public static RandomizedActionIntervalCommand getInstance()
	{
		return INSTANCE;
	}

	public void extendCommand(ArgumentBuilder<ServerCommandSource, ?> node, EntityPlayerActionPack.ActionType type, CommandActionMaker actionMaker)
	{
		Function<Boolean, Command<ServerCommandSource>> uniformCmd = bool -> c -> actionMaker.action(c, type, uniformImpl(c, bool));
		Command<ServerCommandSource> poissonCmd = c -> actionMaker.action(c, type, poissonImpl(c));
		Command<ServerCommandSource> gaussianCmd = c -> actionMaker.action(c, type, gaussianImpl(c));

		node.
				then(literal("randomly").
						executes(c -> randomlyHelp(c.getSource())).
						then(argument("lower_bound", integer(1)).
								then(argument("upper_bound", integer(1)).
										executes(uniformCmd.apply(true))
								)
						).
						then(literal("uniform").
								executes(c -> uniformHelp(c.getSource())).
								then(argument("lower_bound", integer(1)).
										then(argument("upper_bound", integer(1)).
												executes(uniformCmd.apply(false))
										)
								)
						).
						then(literal("poisson").
								executes(c -> poissonHelp(c.getSource())).
								then(argument("origin", doubleArg()).
										then(argument("maxima", doubleArg()).
												executes(poissonCmd).
												then(argument("upper_bound", integer(1)).
														executes(poissonCmd)
												)
										)
								)
						).
						then(literal("gaussian").
								executes(c -> gaussianHelp(c.getSource())).
								then(argument("mu", doubleArg()).
										then(argument("sigma2", doubleArg()).
												executes(gaussianCmd).
												then(argument("lower_bound", integer(1)).
														executes(gaussianCmd).
														then(argument("upper_bound", integer(1)).
																executes(gaussianCmd)
														)
												)
										)
								)
						)
				);
	}

	private static Optional<Integer> getOptionalInteger(CommandContext<?> c, String name)
	{
		try
		{
			return Optional.of(getInteger(c, name));
		}
		catch (IllegalArgumentException e)
		{
			return Optional.empty();
		}
	}

	private EntityPlayerActionPack.Action actionFromRandomGen(RandomGen gen)
	{
		EntityPlayerActionPack.Action action = EntityPlayerActionPack.Action.interval(gen.generateInt());
		((IEntityPlayerActionPackAction) action).setIntervalRandomGenerator(gen);
		return action;
	}

	private int randomlyHelp(ServerCommandSource source)
	{
		Messenger.tell(source, Messenger.tr("randomlyHelp random gen help"));
		return 0;
	}

	private int uniformHelp(ServerCommandSource source)
	{
		Messenger.tell(source, Messenger.tr("uniform random gen help"));
		return 0;
	}

	private EntityPlayerActionPack.Action uniformImpl(CommandContext<ServerCommandSource> c, boolean showDeprecation)
	{
		if (showDeprecation)
		{
			// TODO text
			Messenger.tell(c.getSource(), Messenger.s("deprecated, go use uniform subcommand, will be removed when mc1.21 is out", Formatting.DARK_RED));
		}
		int lower = getInteger(c, "lower_bound");
		int upper = getInteger(c, "upper_bound");
		upper = Math.max(lower, upper);

		RandomGen gen = new UniformGen(lower, upper);
		return actionFromRandomGen(gen);
	}

	private int poissonHelp(ServerCommandSource source)
	{
		Messenger.tell(source, Messenger.tr("poisson random gen help"));
		Messenger.tell(source, Messenger.tr("λ = maxima - origin"));
		return 0;
	}

	private EntityPlayerActionPack.Action poissonImpl(CommandContext<ServerCommandSource> c)
	{
		double origin = getDouble(c, "origin");
		double maxima = getDouble(c, "maxima");
		int upper = getOptionalInteger(c, "upper_bound").orElse(Integer.MAX_VALUE);

		double lambda = maxima - origin;
		if (lambda < 0)
		{
			Messenger.tell(c.getSource(), tr("origin should be smaller than maxima"));
		}

		RandomGen gen = new RangeLimitedGen(new PoissonGen(origin, lambda), 1, upper);
		return actionFromRandomGen(gen);
	}

	private int gaussianHelp(ServerCommandSource source)
	{
		Messenger.tell(source, Messenger.tr("gaussian random gen help"));
		Messenger.tell(source, Messenger.tr("sigma2: σ^2"));
		Messenger.tell(source, Messenger.tr("mu: μ"));
		return 0;
	}

	private EntityPlayerActionPack.Action gaussianImpl(CommandContext<ServerCommandSource> c)
	{
		double mu = getDouble(c, "mu");
		double sigma2 = getDouble(c, "sigma2");
		int lower = getOptionalInteger(c, "lower_bound").orElse(1);
		int upper = getOptionalInteger(c, "upper_bound").orElse(Integer.MAX_VALUE);

		RandomGen gen = new RangeLimitedGen(new GaussianGen(mu, sigma2), lower, upper);
		return actionFromRandomGen(gen);
	}
}
