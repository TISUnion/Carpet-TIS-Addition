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

package carpettisaddition.commands.lifetime;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.lifetime.filter.EntityFilterManager;
import carpettisaddition.commands.lifetime.recorder.LifetimeRecorder;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.commands.lifetime.utils.SpecificDetailMode;
import carpettisaddition.utils.CarpetModUtil;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.EntityType;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.SharedSuggestionProvider.suggest;

public class LifeTimeCommand extends AbstractCommand
{
	public static final String NAME = "lifetime";
	private static final LifeTimeCommand INSTANCE = new LifeTimeCommand();

	private LifeTimeCommand()
	{
		super(NAME);
	}

	public static LifeTimeCommand getInstance()
	{
		return INSTANCE;
	}

	private int checkEntityTypeThen(CommandSourceStack source, @Nullable String entityTypeName, Consumer<EntityType<?>> entityTypeConsumer)
	{
		EntityType<?> entityType;
		// global filter
		if (entityTypeName == null)
		{
			entityType = null;
		}
		// filter for specific entity type
		else
		{
			Optional<EntityType<?>> optionalEntityType = LifeTimeTrackerUtil.getEntityTypeFromName(entityTypeName);
			if (optionalEntityType.isPresent())
			{
				entityType = optionalEntityType.get();
			}
			else
			{
				LifeTimeTracker.getInstance().sendUnknownEntity(source, entityTypeName);
				return 0;
			}
		}
		entityTypeConsumer.accept(entityType);
		return 1;
	}

	private int setEntityFilter(CommandSourceStack source, @Nullable String entityTypeName, EntitySelector selector)
	{
		return checkEntityTypeThen(source, entityTypeName, entityType ->
				EntityFilterManager.getInstance().setEntityFilter(source, entityType, selector)
		);
	}

	private int printEntityFilter(CommandSourceStack source, @Nullable String entityTypeName)
	{
		return checkEntityTypeThen(source, entityTypeName, entityType ->
				EntityFilterManager.getInstance().displayFilter(source, entityType)
		);
	}

	/*
	 * ------------------
	 *   Node factories
	 * ------------------
	 */

	private ArgumentBuilder<CommandSourceStack, ?> createFilterNode(ArgumentBuilder<CommandSourceStack, ?> node, Function<CommandContext<CommandSourceStack>, @Nullable String> entityTypeNameSupplier)
	{
		return node.
				executes(c -> printEntityFilter(c.getSource(), entityTypeNameSupplier.apply(c))).
				then(literal("set").then(
						argument("filter", EntityArgument.entities()).
						executes(c -> setEntityFilter(c.getSource(), entityTypeNameSupplier.apply(c), c.getArgument("filter", EntitySelector.class)))
				)).
				then(literal("clear").executes(c -> setEntityFilter(c.getSource(), entityTypeNameSupplier.apply(c), null)));
	}

	/**
	 * make the node execute something with realtime=false,
	 * then with another extra literal "realtime" input it will execute something with realtime=true
	 */
	private ArgumentBuilder<CommandSourceStack, ?> realtimeActionNode(ArgumentBuilder<CommandSourceStack, ?> node, BiFunction<CommandContext<CommandSourceStack>, Boolean, Integer> action)
	{
		return node.executes(c -> action.apply(c, false)).
				then(literal("realtime").executes(c -> action.apply(c, true)));
	}

	/*
	 * -----------------------
	 *   Node factories ends
	 * -----------------------
	 */

	@Override
	public void registerCommand(CommandTreeContext.Register context)
	{
		final String entityTypeArg = "entity_type";
		final String detailModeArg = "detail";
		LiteralArgumentBuilder<CommandSourceStack> builder = literal(NAME).
				requires((player) -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandLifeTime)).
				executes(c -> LifeTimeTracker.getInstance().showHelp(c.getSource())).
				// lifetime tracking [general tracker stuffs here]
				then(
						LifeTimeTracker.getInstance().getTrackingArgumentBuilder()
				).
				// lifetime filter
				then(
						literal("filter").
						executes(c -> EntityFilterManager.getInstance().displayAllFilters(c.getSource())).
						then(createFilterNode(literal("global"), c -> null)).
						then(createFilterNode(
								argument(entityTypeArg, string()).suggests((c, b) -> suggest(LifeTimeTrackerUtil.getEntityTypeDescriptorStream(), b)),
								c -> getString(c, entityTypeArg)
						))
				).
				// lifetime result display
				then(realtimeActionNode(
						argument(entityTypeArg, string()).suggests((c, b) -> suggest(LifeTimeTracker.getInstance().getAvailableEntityType(), b)).
						then(realtimeActionNode(
								argument(detailModeArg, string()).suggests((c, b) -> suggest(SpecificDetailMode.getSuggestion(), b)),
								// lifetime creeper spawning
								(c, realtime) -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
										c.getSource(), getString(c, entityTypeArg), getString(c, detailModeArg), realtime
								)
						)),
						// lifetime creeper
						(c, realtime) -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
								c.getSource(), getString(c, entityTypeArg), null, realtime
						)
				)).
				// lifetime record
				then(literal("recorder").
						executes(c -> LifetimeRecorder.getInstance().showStatus(c.getSource())).
						then(literal("status").
								executes(c -> LifetimeRecorder.getInstance().showStatus(c.getSource()))
						).
						then(literal("reload").
								requires(LifetimeRecorder.getInstance()::hasPermission).
								executes(c -> LifetimeRecorder.getInstance().reloadConfig(c.getSource()))
						).
						then(literal("enable").
								requires(LifetimeRecorder.getInstance()::hasPermission).
								executes(c -> LifetimeRecorder.getInstance().enableRecording(c.getSource()))
						).
						then(literal("disable").
								requires(LifetimeRecorder.getInstance()::hasPermission).
								executes(c -> LifetimeRecorder.getInstance().disableRecording(c.getSource()))
						)
				).
				then(
						literal("help").
								executes(c -> LifeTimeTracker.getInstance().showHelp(c.getSource()))
				);
		context.dispatcher.register(builder);
	}
}
