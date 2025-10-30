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

package carpettisaddition.commands.common.counter;

import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import com.google.common.collect.Maps;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.SharedSuggestionProvider.suggest;

public abstract class DyeCounterCommand<Key extends DyeCounterKey, Counter extends DyeCounter<Key>> extends AbstractCommand implements DyeCounterProvider<Key, DyeCounter<Key>>
{
	public static final List<String> COLORS = Arrays.stream(DyeColor.values()).map(cl -> cl.getName().toLowerCase()).collect(Collectors.toList());

	protected final Map<DyeColor, Counter> counter = Maps.newEnumMap(DyeColor.class);
	protected final String commandPrefix;
	private final Translator baseTr;

	public DyeCounterCommand(String name, String commandPrefix)
	{
		super(name);
		this.baseTr = new Translator("command.dye_counter");
		this.commandPrefix = commandPrefix;
		for (DyeColor color : DyeColor.values())
		{
			this.counter.put(color, this.createCounterForColor(color));
		}
	}

	@NotNull
	@Override
	public Counter getCounter(@NotNull DyeColor color)
	{
		return this.counter.get(Objects.requireNonNull(color));
	}

	@Nullable
	@Override
	public Counter getCounter(String color)
	{
		DyeColor dyeColor;
		try
		{
			dyeColor = DyeColor.valueOf(color.toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			return null;
		}
		return this.getCounter(dyeColor);
	}

	protected abstract Counter createCounterForColor(DyeColor color);

	protected abstract Object getRuleValue();

	protected abstract boolean isActivated();

	protected void addFor(DyeColor color, Key key, long count)
	{
		this.counter.get(color).addForKey(key, count);
	}

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		LiteralArgumentBuilder<CommandSourceStack> root = literal(this.commandPrefix).
				requires(s -> CarpetModUtil.canUseCommand(s, this.getRuleValue())).
				executes(c -> reportAll(c.getSource())).
				then(literal("reset").
						executes(c -> resetAll(c.getSource()))
				).
				then(argument("color", word()).
						suggests((c, b) -> suggest(COLORS, b)).
						executes(c -> report(c.getSource(), getString(c, "color"), false)).
						then(literal("realtime").
								executes(c -> report(c.getSource(), getString(c, "color"), true))
						).
						then(literal("reset").
								executes(c -> resetSingle(c.getSource(), getString(c, "color")))
						)
				);
		context.dispatcher.register(root);
	}

	private int report(CommandSourceStack source, String color, boolean realtime)
	{
		return this.doWithCounter(source, color, counter -> {
			Messenger.tell(source, counter.report(realtime));
			return 1;
		});
	}

	private int reportAll(CommandSourceStack source)
	{
		int printed = 0;

		for (Counter counter : this.counter.values())
		{
			List<BaseComponent> lines = counter.report(false);

			if (lines.size() > 1)
			{
				Messenger.tell(source, Messenger.s(""));
				printed++;
				Messenger.tell(source, lines);
			}
		}

		if (printed == 0)
		{
			Messenger.tell(source, this.baseTr.tr("no_item_yet", tr("key_name")));
		}

		return printed;
	}

	private int doWithCounter(CommandSourceStack source, String color, Function<Counter, Integer> consumer)
	{
		return Optional.ofNullable(this.getCounter(color)).
				map(consumer).
				orElseGet(() -> {
					Messenger.tell(source, this.baseTr.tr("unknown_color", color));
					return 0;
				});
	}

	private int resetAll(CommandSourceStack source)
	{
		for (Counter counter : this.counter.values())
		{
			counter.reset();
		}
		Messenger.tell(source, this.baseTr.tr("restart_all", tr("counter_name")), true);
		return 1;
	}

	private int resetSingle(CommandSourceStack source, String color)
	{
		return this.doWithCounter(source, color, counter -> {
			counter.reset();
			Messenger.tell(source, this.baseTr.tr("restart_single", counter.getColorText(), tr("counter_name")), true);
			return 1;
		});
	}
}
