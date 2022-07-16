package carpettisaddition.commands.manipulate;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.commands.AbstractCommand;
import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.container.ContainerManipulator;
import carpettisaddition.commands.manipulate.entity.EntityManipulator;
import carpettisaddition.utils.CarpetModUtil;
import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class ManipulateCommand extends AbstractCommand
{
	private static final String NAME = "manipulate";
	private static final ManipulateCommand INSTANCE = new ManipulateCommand();

	private static final List<AbstractManipulator> MANIPULATORS = ImmutableList.of(
			ContainerManipulator.getInstance(),
			new EntityManipulator()
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
	public void registerCommand(CommandTreeContext.Register context)
	{
		LiteralArgumentBuilder<ServerCommandSource> root = literal(NAME).
			requires(player -> CarpetModUtil.canUseCommand(player, CarpetTISAdditionSettings.commandManipulate));

		MANIPULATORS.forEach(m -> {
			LiteralArgumentBuilder<ServerCommandSource> child = literal(m.getName());
			m.buildSubCommand(context.node(child));
			root.then(child);
		});

		context.dispatcher.register(root);
	}
}
