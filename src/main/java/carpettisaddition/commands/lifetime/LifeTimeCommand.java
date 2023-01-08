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
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.commands.lifetime.utils.SpecificDetailMode;
import carpettisaddition.utils.CarpetModUtil;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.EntityType;
import net.minecraft.server.command.ServerCommandSource;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandSource.suggestMatching;

public class LifeTimeCommand extends AbstractCommand
{
	private static final String NAME = "lifetime";
	private static final LifeTimeCommand INSTANCE = new LifeTimeCommand();

	private LifeTimeCommand()
	{
		super(NAME);
	}

	public static LifeTimeCommand getInstance()
	{
		return INSTANCE;
	}

	private int checkEntityTypeThen(ServerCommandSource source, @Nullable String entityTypeName, Consumer<EntityType<?>> entityTypeConsumer)
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

	private int setEntityFilter(ServerCommandSource source, @Nullable String entityTypeName, EntitySelector selector)
	{
		return checkEntityTypeThen(source, entityTypeName, entityType ->
				EntityFilterManager.getInstance().setEntityFilter(source, entityType, selector)
		);
	}

	private int printEntityFilter(ServerCommandSource source, @Nullable String entityTypeName)
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

	private ArgumentBuilder<ServerCommandSource, ?> createFilterNode(ArgumentBuilder<ServerCommandSource, ?> node, Function<CommandContext<ServerCommandSource>, @Nullable String> entityTypeNameSupplier)
	{
		return node.
				executes(c -> printEntityFilter(c.getSource(), entityTypeNameSupplier.apply(c))).
				then(literal("set").then(
						argument("filter", EntityArgumentType.entities()).
						executes(c -> setEntityFilter(c.getSource(), entityTypeNameSupplier.apply(c), c.getArgument("filter", EntitySelector.class)))
				)).
				then(literal("clear").executes(c -> setEntityFilter(c.getSource(), entityTypeNameSupplier.apply(c), null)));
	}

	/**
	 * make the node execute something with realtime=false,
	 * then with another extra literal "realtime" input it will execute something with realtime=true
	 */
	private ArgumentBuilder<ServerCommandSource, ?> realtimeActionNode(ArgumentBuilder<ServerCommandSource, ?> node, BiFunction<CommandContext<ServerCommandSource>, Boolean, Integer> action)
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
		LiteralArgumentBuilder<ServerCommandSource> builder = literal(NAME).
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
								argument(entityTypeArg, string()).suggests((c, b) -> suggestMatching(LifeTimeTrackerUtil.getEntityTypeDescriptorStream(), b)),
								c -> getString(c, entityTypeArg)
						))
				).
				// lifetime result display
				then(realtimeActionNode(
						argument(entityTypeArg, string()).suggests((c, b) -> suggestMatching(LifeTimeTracker.getInstance().getAvailableEntityType(), b)).
						then(realtimeActionNode(
								argument(detailModeArg, string()).suggests((c, b) -> suggestMatching(SpecificDetailMode.getSuggestion(), b)),
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
				then(
						literal("help").
								executes(c -> LifeTimeTracker.getInstance().showHelp(c.getSource()))
				);
		context.dispatcher.register(builder);
	}
}
