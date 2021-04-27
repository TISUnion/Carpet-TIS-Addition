package carpettisaddition.commands.lifetime;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
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

import java.util.Optional;
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

	private int setEntityFilter(ServerCommandSource source, String entityTypeName, EntitySelector selector)
	{
		Optional<EntityType<?>> optionalEntityType = LifeTimeTrackerUtil.getEntityTypeFromName(entityTypeName);
		if (optionalEntityType.isPresent())
		{
			LifeTimeTracker.getInstance().setEntityFilter(source, optionalEntityType.get(), selector);
			return 1;
		}
		else
		{
			LifeTimeTracker.getInstance().sendUnknownEntity(source, entityTypeName);
			return 0;
		}
	}

	private ArgumentBuilder<ServerCommandSource, ?> createFilterNode(ArgumentBuilder<ServerCommandSource, ?> node, Function<CommandContext<ServerCommandSource>, EntityType<?>> entityTypeSupplier)
	{
		return node.
				executes(c -> LifeTimeTracker.getInstance().setEntityFilter(c.getSource(), entityTypeSupplier.apply(c), null)).
				then(
						argument("filter", EntityArgumentType.entities()).
						executes(c -> LifeTimeTracker.getInstance().setEntityFilter(c.getSource(), entityTypeSupplier.apply(c), c.getArgument("filter", EntitySelector.class)))
				);
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		String entityTypeArg = "entity_type";
		String detailModeArg = "detail";
		LiteralArgumentBuilder<ServerCommandSource> builder = literal(NAME).
				requires((player) -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandLifeTime)).
				// lifetime tracking [general tracker stuffs here]
				then(
						LifeTimeTracker.getInstance().getTrackingArgumentBuilder()
				).
				// lifetime filter
				then(
						literal("filter").
						then(
								literal("global").
								executes(c -> LifeTimeTracker.getInstance().setEntityFilter(c.getSource(), null, null)).
								then(
										argument("filter", EntityArgumentType.entities()).
										executes(c -> LifeTimeTracker.getInstance().setEntityFilter(c.getSource(), null, c.getArgument("filter", EntitySelector.class)))
								)
						).
						then(
								argument(entityTypeArg, string()).
								suggests((c, b) -> suggestMatching(LifeTimeTrackerUtil.getEntityTypeDescriptorStream(), b)).
								executes(c -> setEntityFilter(c.getSource(), getString(c, entityTypeArg), null)).
								then(
										argument("filter", EntityArgumentType.entities()).
										executes(c -> setEntityFilter(c.getSource(), getString(c, entityTypeArg), c.getArgument("filter", EntitySelector.class)))
								)
						)
				).
				// lifetime result display
				then(
						argument(entityTypeArg, string()).
						suggests((c, b) -> suggestMatching(LifeTimeTracker.getInstance().getAvailableEntityType(), b)).
								// lifetime tracking creeper
								executes(c -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
										c.getSource(), getString(c, entityTypeArg), null, false)
								).
								then(
										// lifetime tracking creeper realtime
										literal("realtime").
												executes(c -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
														c.getSource(), getString(c, entityTypeArg), null, true)
												)
								).
								then(
										argument(detailModeArg, string()).
										suggests((c, b) -> suggestMatching(SpecificDetailMode.getSuggestion(), b)).
												// lifetime tracking creeper spawning
												executes(c -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
														c.getSource(),
														getString(c, entityTypeArg),
														getString(c, detailModeArg),
														false
												)).
												then(
														// lifetime tracking creeper spawning realtime
														literal("realtime").
																executes(c -> LifeTimeTracker.getInstance().printTrackingResultSpecific(
																		c.getSource(),
																		getString(c, entityTypeArg),
																		getString(c, detailModeArg),
																		true)
																)
												)
								)
				).
				then(
						literal("help").
								executes(c -> LifeTimeTracker.getInstance().showHelp(c.getSource()))
				);
		dispatcher.register(builder);
	}
}
