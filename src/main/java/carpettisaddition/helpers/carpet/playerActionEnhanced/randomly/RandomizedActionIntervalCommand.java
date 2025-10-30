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
import com.google.common.base.Strings;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

import static com.mojang.brigadier.arguments.DoubleArgumentType.doubleArg;
import static com.mojang.brigadier.arguments.DoubleArgumentType.getDouble;
import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class RandomizedActionIntervalCommand extends TranslationContext
{
	private static final RandomizedActionIntervalCommand INSTANCE = new RandomizedActionIntervalCommand();

	private RandomizedActionIntervalCommand()
	{
		super("command.player.action.randomly");
	}

	public static RandomizedActionIntervalCommand getInstance()
	{
		return INSTANCE;
	}

	public void extendCommand(ArgumentBuilder<CommandSourceStack, ?> node, EntityPlayerActionPack.ActionType type, CommandActionMaker actionMaker)
	{
		Function<Boolean, Command<CommandSourceStack>> uniformCmd = bool -> c -> actionMaker.action(c, type, uniformImpl(c, bool));
		Command<CommandSourceStack> poissonCmd = c -> actionMaker.action(c, type, poissonImpl(c));
		Command<CommandSourceStack> gaussianCmd = c -> actionMaker.action(c, type, gaussianImpl(c));
		Command<CommandSourceStack> triangularCmd = c -> actionMaker.action(c, type, triangularImpl(c));
		EndpointCmdMaker endpoint = (n, cmd, g) -> n.
				executes(cmd).
				then(literal("--simulate").
						executes(c -> simulateRun(c, g.get(c)))
				);

		node.
				then(literal("randomly").
						executes(c -> randomlyHelp(c.getSource())).
						// deprecated, remove this in mc1.21
						then(argument("min", integer(1)).
								then(argument("max", integer(1)).
										executes(uniformCmd.apply(true))
								)
						).
						then(literal("uniform").
								executes(c -> uniformHelp(c.getSource())).
								then(argument("min", integer(1)).
										then(endpoint.make(argument("max", integer(1)),
												uniformCmd.apply(false),
												this::uniformGen
										))
								)
						).
						then(literal("poisson").
								executes(c -> poissonHelp(c.getSource())).
								then(argument("origin", doubleArg()).
										then(endpoint.make(argument("maxima", doubleArg()).
												then(endpoint.make(argument("upper_bound", integer(1)),
														poissonCmd, this::poissonGen
												)),
												poissonCmd, this::poissonGen
										))
								)
						).
						then(literal("gaussian").
								executes(c -> gaussianHelp(c.getSource())).
								then(argument("mu", doubleArg()).
										then(endpoint.make(argument("sigma", doubleArg(0)).
														then(endpoint.make(argument("lower_bound", integer(1)).
																		then(endpoint.make(argument("upper_bound", integer(1)),
																				gaussianCmd, this::gaussianGen
																		)),
																gaussianCmd, this::gaussianGen
														)),
												gaussianCmd, this::gaussianGen
										))
								)
						).
						then(literal("triangular").
								executes(c -> triangularHelp(c.getSource())).
								then(argument("mode", doubleArg()).
										then(endpoint.make(argument("deviation", doubleArg(0)).
														then(endpoint.make(argument("lower_bound", integer(1)).
																		then(endpoint.make(argument("upper_bound", integer(1)),
																				triangularCmd, this::triangularGen
																		)),
																triangularCmd, this::triangularGen
														)),
												triangularCmd, this::triangularGen
										))
								)
						)
				);
	}

	@FunctionalInterface
	private interface RandomGenSupplier
	{
		RandomGen get(CommandContext<CommandSourceStack> c) throws CommandSyntaxException;
	}

	@FunctionalInterface
	private interface EndpointCmdMaker
	{
		ArgumentBuilder<CommandSourceStack, ?> make(ArgumentBuilder<CommandSourceStack, ?> node, Command<CommandSourceStack> cmd, RandomGenSupplier genSupplier);
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
		EntityPlayerActionPack.Action action = EntityPlayerActionPack.Action.interval(gen.generateRandomInterval());
		((IEntityPlayerActionPackAction)action).setIntervalRandomGenerator$TISCM(gen);
		return action;
	}

	private int randomlyHelp(CommandSourceStack source)
	{
		Messenger.tell(source, tr("root.help"));
		return 0;
	}

	private int simulateRun(CommandContext<CommandSourceStack> c, RandomGen gen)
	{
		final int CHAR_COUNT = 40;
		final int MIN_N = 10000;
		final int MAX_N = 1000000;
		final long MAX_MILLI = 500;  // 500ms

		CommandSourceStack source = c.getSource();
		Int2IntOpenHashMap counter = new Int2IntOpenHashMap();
		int n = 0;

		long start = System.currentTimeMillis();
		for (int i = 0; i < MAX_N; i++)
		{
			if (i >= MIN_N && i % 100 == 0)
			{
				long now = System.currentTimeMillis();
				if (now - start > MAX_MILLI)
				{
					break;
				}
			}
			counter.addTo(gen.generateRandomInterval(), 1);
			n++;
		}

		int maxCount = counter.values().
				//#if MC >= 11800
				//$$ intStream().
				//#else
				stream().mapToInt(x -> x).
				//#endif
				max().orElse(1);
		int maxKeyWidth = counter.keySet().
				//#if MC >= 11800
				//$$ intStream().map
				//#else
				stream().mapToInt
				//#endif
						(x -> String.valueOf(x).length()).max().orElse(3);

		Messenger.tell(source, Messenger.click(
				tr("simulate.command", c.getInput()),
				Messenger.ClickEvents.suggestCommand(c.getInput())
		));
		Messenger.tell(source, tr("simulate.samples", n));
		Messenger.tell(source, Messenger.formatting(tr("simulate.divider"), ChatFormatting.GRAY));
		int finalN = n;
		counter.int2IntEntrySet().stream().
				sorted(Comparator.comparing(Int2IntMap.Entry::getIntKey)).
				forEach(e -> {
					final String fmt = "%" + maxKeyWidth + "d %5.1f%% %s";
					Messenger.tell(source, Messenger.s(String.format(
							fmt,
							e.getIntKey(), 100.0 * e.getIntValue() / finalN,
							Strings.repeat("#", CHAR_COUNT * e.getIntValue() / maxCount)
					)));
				});
		Messenger.tell(source, Messenger.formatting(tr("simulate.divider"), ChatFormatting.GRAY));

		return n;
	}

	// ========================== uniform ==========================

	private int uniformHelp(CommandSourceStack source)
	{
		Messenger.tell(source, tr("uniform.help"));
		return 0;
	}

	private RandomGen uniformGen(CommandContext<CommandSourceStack> c)
	{
		int lower = getInteger(c, "min");
		int upper = getInteger(c, "max");
		upper = Math.max(lower, upper);

		return new UniformGen(lower, upper);
	}

	private EntityPlayerActionPack.Action uniformImpl(CommandContext<CommandSourceStack> c, boolean showDeprecation)
	{
		if (showDeprecation)
		{
			Messenger.tell(c.getSource(), Messenger.s(tr("root.uniform_deprecation"), ChatFormatting.DARK_RED));
		}
		return actionFromRandomGen(uniformGen(c));
	}

	// ========================== poisson ==========================

	private int poissonHelp(CommandSourceStack source)
	{
		Messenger.tell(source, tr("poisson.help"));
		return 0;
	}

	private RandomGen poissonGen(CommandContext<CommandSourceStack> c) throws CommandSyntaxException
	{
		double origin = getDouble(c, "origin");
		double maxima = getDouble(c, "maxima");
		int upper = getOptionalInteger(c, "upper_bound").orElse(Integer.MAX_VALUE);

		double lambda = maxima - origin;
		if (lambda < 0)
		{
			throw new SimpleCommandExceptionType(tr("poisson.large_origin")).create();
		}

		// poisson gen with lambda >= 50 is almost equal to gaussian gen
		// we do the replacement here, cuz the poisson gen impl is slow when lambda is large
		RandomGen gen =
				lambda <= 50 ?
				new PoissonGen(origin, lambda) :
				new RangeLimitedGen(new GaussianGen(origin + lambda, Math.sqrt(lambda)), (int)Math.round(origin), Integer.MAX_VALUE);

		return new RangeLimitedGen(gen, Integer.MIN_VALUE, upper);
	}

	private EntityPlayerActionPack.Action poissonImpl(CommandContext<CommandSourceStack> c) throws CommandSyntaxException
	{
		return actionFromRandomGen(poissonGen(c));
	}

	// ========================== gaussian ==========================

	private int gaussianHelp(CommandSourceStack source)
	{
		Messenger.tell(source, tr("gaussian.help"));
		return 0;
	}

	private RandomGen gaussianGen(CommandContext<CommandSourceStack> c)
	{
		double mu = getDouble(c, "mu");
		double sigma = getDouble(c, "sigma");
		int lower = getOptionalInteger(c, "lower_bound").orElse(Integer.MIN_VALUE);
		int upper = getOptionalInteger(c, "upper_bound").orElse(Integer.MAX_VALUE);

		return new RangeLimitedGen(new GaussianGen(mu, sigma), lower, upper);
	}

	private EntityPlayerActionPack.Action gaussianImpl(CommandContext<CommandSourceStack> c)
	{
		return actionFromRandomGen(gaussianGen(c));
	}

	// ========================== triangular ==========================

	private int triangularHelp(CommandSourceStack source)
	{
		Messenger.tell(source, tr("triangular.help"));
		return 0;
	}

	private RandomGen triangularGen(CommandContext<CommandSourceStack> c)
	{
		double mode = getDouble(c, "mode");
		double deviation = getDouble(c, "deviation");
		int lower = getOptionalInteger(c, "lower_bound").orElse(Integer.MIN_VALUE);
		int upper = getOptionalInteger(c, "upper_bound").orElse(Integer.MAX_VALUE);

		return new RangeLimitedGen(new TriangularGen(mode, deviation), lower, upper);
	}

	private EntityPlayerActionPack.Action triangularImpl(CommandContext<CommandSourceStack> c)
	{
		return actionFromRandomGen(triangularGen(c));
	}
}
