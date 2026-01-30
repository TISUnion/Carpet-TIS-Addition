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

package carpettisaddition.commands.spawn.natualSpawning;

import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandExtender;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.GameUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;
import static com.mojang.brigadier.arguments.BoolArgumentType.getBool;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.DimensionTypeArgument.dimension;
import static net.minecraft.commands.arguments.DimensionTypeArgument.getDimension;

public class SpawnNatualSpawningCommand extends AbstractCommand implements CommandExtender
{
	private static final String NAME = "natualSpawning";
	private static final SpawnNatualSpawningCommand INSTANCE = new SpawnNatualSpawningCommand();

	private final Set<NatualSpawningUint> natualSpawningDisabled = Sets.newHashSet();

	private SpawnNatualSpawningCommand()
	{
		super("spawn.natualSpawning");
	}

	public static SpawnNatualSpawningCommand getInstance()
	{
		return INSTANCE;
	}

	private boolean shouldSpawn(NatualSpawningUint unit)
	{
		return !this.natualSpawningDisabled.contains(unit);
	}

	public boolean shouldSpawn(@NotNull DimensionWrapper dimension, @NotNull MobCategory catalogue)
	{
		return this.shouldSpawn(new NatualSpawningUint(dimension, catalogue));
	}

	@Override
	public void extendCommand(CommandTreeContext.Node context)
	{
		// "/spawn natualSpawning [at <dimension>] [for <catalogue>] [show] [set <value> [--show]] [resetAll]"

		Command<CommandSourceStack> showCmd = c -> showNatualSpawningState(c.getSource(), new NatualSpawningUintCondition(c));
		Command<CommandSourceStack> showCurrentCmd = c -> showNatualSpawningState(c.getSource(), new NatualSpawningUintCondition(c, DimensionWrapper.of(c.getSource().getLevel())));
		Command<CommandSourceStack> setCmd = c -> setNatualSpawningState(c.getSource(), new NatualSpawningUintCondition(c), getBool(c, "shouldSpawn"), true);
		Command<CommandSourceStack> setAndShowCmd = c -> setNatualSpawningState(c.getSource(), new NatualSpawningUintCondition(c), getBool(c, "shouldSpawn"), false);

		LiteralCommandNode<CommandSourceStack> subcommandRoot = literal(NAME).build();
		context.node.then(subcommandRoot);

		context.node.then(literal(NAME).
				executes(showCmd).
				then(literal("show").
						executes(showCmd)
				).
				then(literal("resetAll").
						executes(c -> resetAllNatualSpawningState(c.getSource()))
				).
				then(literal("set").
						then(argument("shouldSpawn", bool()).
								executes(setCmd).
								then(literal("--show").
										executes(setAndShowCmd)
								)
						)
				).
				then(literal("at").
						then(literal("current").
								executes(showCurrentCmd).
								redirect(subcommandRoot, CommandUtils.preservingRedirectWithArg("currentDimension", true))
						).
						then(argument("dimension", dimension()).
								executes(showCmd).
								redirect(subcommandRoot, CommandUtils.preservingRedirect())
						)
				).
				then(literal("for").
						then(mobCategoryArgument("catalogue").
								executes(showCmd).
								redirect(subcommandRoot, CommandUtils.preservingRedirect())
						)
				)
		);
	}

	@SuppressWarnings("SameParameterValue")
	private RequiredArgumentBuilder<CommandSourceStack, String> mobCategoryArgument(String name)
	{
		return CommandUtils.enumArg(name, MobCategory.class);
	}

	@SuppressWarnings("SameParameterValue")
	private MobCategory getMobCategory(CommandContext<CommandSourceStack> ctx, String name) throws CommandSyntaxException
	{
		Either<MobCategory, String> ev = CommandUtils.getEnum(ctx, name, MobCategory.class);
		return ev.left().orElseThrow(() -> {
			BaseComponent msg = Messenger.formatting(tr("unknown_enum.mob_catalogue", ev.right().orElse("?")), ChatFormatting.RED);
			return new SimpleCommandExceptionType(msg).create();
		});
	}

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private class NatualSpawningUintCondition
	{
		public final @Nullable DimensionWrapper dimension;
		public final @Nullable MobCategory catalogue;

		public NatualSpawningUintCondition(@Nullable DimensionWrapper dimension, @Nullable MobCategory catalogue)
		{
			this.dimension = dimension;
			this.catalogue = catalogue;
		}

		public NatualSpawningUintCondition(CommandContext<CommandSourceStack> ctx, DimensionWrapper currentDimension) throws CommandSyntaxException
		{
			this(
					CommandUtils.getOptArg(() -> DimensionWrapper.of(getDimension(ctx, "dimension"))).orElse(currentDimension),
					CommandUtils.getOptArg(() -> getMobCategory(ctx, "catalogue")).orElse(null)
			);
		}

		public NatualSpawningUintCondition(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException
		{
			this(ctx, CommandUtils.getOptArg(() -> getBool(ctx, "currentDimension")).
					map(b -> DimensionWrapper.of(ctx.getSource().getLevel())).
					orElse(null)
			);
		}

		public boolean test(DimensionWrapper dimension)
		{
			return Optional.ofNullable(this.dimension).map(dimension::equals).orElse(true);
		}

		public boolean test(MobCategory catalogue)
		{
			return Optional.ofNullable(this.catalogue).map(catalogue::equals).orElse(true);
		}

		public boolean targetsAllDimensions()
		{
			return this.dimension == null;
		}

		@SuppressWarnings("ConstantValue")
		@Nullable
		public BaseComponent toText()
		{
			if (this.dimension == null && this.catalogue == null)
			{
				return null;
			}
			else if (this.dimension != null && this.catalogue == null)
			{
				return tr("condition.dimension_only", Messenger.dimension(this.dimension));
			}
			else if (this.dimension == null && this.catalogue != null)
			{
				return tr("condition.catalogue_only", Messenger.mobCategoryColored(this.catalogue));
			}
			else
			{
				return tr("condition.dimension_and_catalogue", Messenger.dimension(this.dimension), Messenger.mobCategoryColored(this.catalogue));
			}
		}

		public BaseComponent toSuffixText()
		{
			BaseComponent text = this.toText();
			return text != null ? Messenger.c(Messenger.s(" ("), text, Messenger.s(")")) : Messenger.s("");
		}
	}

	private static String makeSetAndShowCommand(@Nullable DimensionWrapper dimension, @Nullable MobCategory mobCategory, boolean value)
	{
		StringBuilder builder = new StringBuilder("/spawn ").append(NAME);
		if (dimension != null)
		{
			builder.append(" at ").append(dimension.getIdentifierString());
		}
		if (mobCategory != null)
		{
			builder.append(" for ").append(mobCategory.getName().toLowerCase());
		}
		builder.append(" set ").append(value);
		builder.append(" --show");
		return builder.toString();
	}

	private boolean hasAnySpawnable(MinecraftServer server, @NotNull MobCategory catalogue)
	{
		return GameUtils.listWorlds(server).stream().
				map(DimensionWrapper::of).
				anyMatch(dimension -> this.shouldSpawn(new NatualSpawningUint(dimension, catalogue)));
	}

	private static final List<MobCategory> SPAWNING_MOB_CATEGORIES = Arrays.stream(MobCategory.values()).
			filter(c -> c != MobCategory.MISC).
			collect(Collectors.toList());

	private void showNatualSpawningStateOneDimension(CommandSourceStack source, NatualSpawningUintCondition condition, @Nullable DimensionWrapper dimension)
	{
		final boolean isAllDimension = dimension == null;

		List<BaseComponent> states = Lists.newArrayList();
		boolean hasSpawnable = false;
		for (MobCategory mobCategory : SPAWNING_MOB_CATEGORIES)
		{
			if (!condition.test(mobCategory))
			{
				continue;
			}

			BaseComponent mct = Messenger.mobCategory(mobCategory);

			boolean spawnEnableForThis = dimension != null
					? this.shouldSpawn(dimension, mobCategory)
					: this.hasAnySpawnable(source.getServer(), mobCategory);
			if (spawnEnableForThis)
			{
				hasSpawnable = true;
				if (isAllDimension)
				{
					states.add(Messenger.fancy(
							Messenger.formatting(mct, ChatFormatting.WHITE),
							tr("show.catalogue.enabled", Messenger.mobCategoryColored(mobCategory)),
							Messenger.ClickEvents.runCommand(makeSetAndShowCommand(null, mobCategory, false))
					));
				}
				else
				{
					states.add(Messenger.fancy(
							Messenger.formatting(mct, ChatFormatting.WHITE),
							tr("show.single.enabled", Messenger.formatting(tr("show.enabled"), ChatFormatting.GREEN), Messenger.dimensionColored(dimension), Messenger.mobCategoryColored(mobCategory)),
							Messenger.ClickEvents.runCommand(makeSetAndShowCommand(dimension, mobCategory, false))
					));
				}
			}
			else
			{
				if (isAllDimension)
				{
					states.add(Messenger.fancy(
							Messenger.formatting(mct, ChatFormatting.DARK_GRAY, ChatFormatting.STRIKETHROUGH),
							tr("show.catalogue.disabled", Messenger.mobCategoryColored(mobCategory)),
							Messenger.ClickEvents.runCommand(makeSetAndShowCommand(null, mobCategory, true))
					));
				}
				else
				{
					states.add(Messenger.fancy(
							Messenger.formatting(mct, ChatFormatting.DARK_GRAY, ChatFormatting.STRIKETHROUGH),
							tr("show.single.disabled", Messenger.formatting(tr("show.disabled"), ChatFormatting.RED), Messenger.dimensionColored(dimension), Messenger.mobCategoryColored(mobCategory)),
							Messenger.ClickEvents.runCommand(makeSetAndShowCommand(dimension, mobCategory, true))
					));
				}
			}
		}

		BaseComponent dimensionText;
		if (isAllDimension)
		{
			dimensionText = Messenger.fancy(
					Messenger.c(
							Messenger.s("[", ChatFormatting.GRAY),
							Messenger.formatting(tr("show.all_dimension"), ChatFormatting.BOLD),
							Messenger.s("]", ChatFormatting.GRAY)
					),
					hasSpawnable
							? Messenger.formatting(tr("show.all.enabled"), ChatFormatting.GREEN)
							: Messenger.formatting(tr("show.all.disabled"), ChatFormatting.RED),
					Messenger.ClickEvents.runCommand(makeSetAndShowCommand(null, null, !hasSpawnable))
			);
		}
		else
		{
			dimensionText = Messenger.fancy(
					Messenger.c(
							Messenger.s("[", ChatFormatting.GRAY),
							Messenger.dimensionColored(dimension),
							Messenger.s("]", ChatFormatting.GRAY)
					),
					hasSpawnable
							? tr("show.dimension.enabled", Messenger.dimensionColored(dimension))
							: tr("show.dimension.disabled", Messenger.dimensionColored(dimension)),
					Messenger.ClickEvents.runCommand(makeSetAndShowCommand(dimension, null, !hasSpawnable))
			);
		}

		Messenger.tell(source, Messenger.c(
				Messenger.s("- ", ChatFormatting.DARK_GRAY),
				dimensionText,
				Messenger.s(": ", ChatFormatting.GRAY),
				Messenger.join(Messenger.s(" "), states)
		));
	}

	private void showNatualSpawningStateInner(CommandSourceStack source, NatualSpawningUintCondition condition)
	{
		if (condition.targetsAllDimensions())
		{
			showNatualSpawningStateOneDimension(source, condition, null);
		}
		for (ServerLevel level : GameUtils.listWorlds(source.getServer()))
		{
			DimensionWrapper dimension = DimensionWrapper.of(level);
			if (condition.test(dimension))
			{
				showNatualSpawningStateOneDimension(source, condition, dimension);
			}
		}
	}

	private int showNatualSpawningState(CommandSourceStack source, NatualSpawningUintCondition condition)
	{
		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, tr("show.show", condition.toSuffixText()));
		this.showNatualSpawningStateInner(source, condition);
		return 0;
	}

	private int setNatualSpawningState(CommandSourceStack source, NatualSpawningUintCondition condition, boolean shouldSpawn, boolean showAfterSet)
	{
		int affectedDimensionCount = 0, affectedategoryCount = 0;
		List<BaseComponent> valueChanges = Lists.newArrayList();
		for (Level level : GameUtils.listWorlds(source.getServer()))
		{
			DimensionWrapper dimension = DimensionWrapper.of(level);
			if (!condition.test(dimension))
			{
				continue;
			}
			affectedDimensionCount++;

			for (MobCategory mobCategory : SPAWNING_MOB_CATEGORIES)
			{
				if (!condition.test(mobCategory))
				{
					continue;
				}
				NatualSpawningUint unit = new NatualSpawningUint(dimension, mobCategory);
				boolean wasSpawningEnabled = this.shouldSpawn(unit);
				if (shouldSpawn)
				{
					this.natualSpawningDisabled.remove(unit);
				}
				else
				{
					this.natualSpawningDisabled.add(unit);
				}

				affectedategoryCount++;
				valueChanges.add(Messenger.format(
						"%s %s: %s -> %s",
						Messenger.dimension(dimension),
						Messenger.mobCategoryColored(mobCategory),
						Messenger.bool(wasSpawningEnabled),
						Messenger.bool(shouldSpawn)
				));
			}
		}
		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, tr("set.done", Messenger.s(affectedDimensionCount, ChatFormatting.GOLD), Messenger.s(affectedategoryCount, ChatFormatting.YELLOW)));
		for (BaseComponent text : valueChanges)
		{
			Messenger.tell(source, Messenger.c(Messenger.s("- ", ChatFormatting.DARK_GRAY), text));
		}

		if (showAfterSet)
		{
			Messenger.tell(source, Messenger.s(""));
			Messenger.tell(source, tr("set.show_after_set"));
			this.showNatualSpawningStateInner(source, new NatualSpawningUintCondition((DimensionWrapper)null, null));
		}

		return 0;
	}

	private int resetAllNatualSpawningState(CommandSourceStack source)
	{
		this.natualSpawningDisabled.clear();
		Messenger.tell(source, Messenger.s(""));
		Messenger.tell(source, tr("resetAll.done"));
		return 0;
	}
}
