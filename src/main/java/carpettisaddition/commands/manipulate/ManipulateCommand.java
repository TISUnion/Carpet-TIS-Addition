package carpettisaddition.commands.manipulate;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.manipulate.container.*;
import carpettisaddition.utils.CarpetModUtil;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class ManipulateCommand extends AbstractCommand
{
	private static final String NAME = "manipulate";
	private static final ManipulateCommand INSTANCE = new ManipulateCommand();
	private static final List<AbstractContainerController> COLLECTION_CONTROLLERS = ImmutableList.of(
			new EntityListController(),
			new TileEntityListController(),
			new TileTickQueueController(),
			new BlockEventQueueController()
	);

	public ManipulateCommand()
	{
		super(NAME);
	}

	public static ManipulateCommand getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void registerCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> containerNode = literal("container");
		COLLECTION_CONTROLLERS.forEach(controller -> containerNode.then(controller.getCommandNode()));

		LiteralArgumentBuilder<ServerCommandSource> builder = literal(NAME).
			requires(player -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandManipulate)).
			then(containerNode);

		dispatcher.register(builder);
	}
}
