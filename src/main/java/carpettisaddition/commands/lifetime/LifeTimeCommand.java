package carpettisaddition.commands.lifetime;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.lifetime.filter.EntityFilterManager;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.commands.lifetime.utils.SpecificDetailMode;
import carpettisaddition.utils.CarpetModUtil;
import com.mojang.brigadier.CommandDispatcher;
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
import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
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
								// lifetime tracking creeper spawning
								(c, realtime) -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
										c.getSource(), getString(c, entityTypeArg), getString(c, detailModeArg), realtime
								)
						)),
						// lifetime tracking creeper
						(c, realtime) -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
								c.getSource(), getString(c, entityTypeArg), null, realtime
						)
				)).
				then(
						literal("help").
								executes(c -> LifeTimeTracker.getInstance().showHelp(c.getSource()))
				);
		dispatcher.register(builder);
	}
}
