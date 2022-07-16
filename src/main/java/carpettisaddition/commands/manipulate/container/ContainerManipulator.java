package carpettisaddition.commands.manipulate.container;

import carpettisaddition.commands.CommandTreeContext;
import carpettisaddition.commands.manipulate.AbstractManipulator;
import carpettisaddition.commands.manipulate.container.controller.*;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class ContainerManipulator extends AbstractManipulator
{
	private static final ContainerManipulator INSTANCE = new ContainerManipulator();
	private static final List<AbstractContainerController> COLLECTION_CONTROLLERS = ImmutableList.of(
			new EntityListController(),
			new TileEntityListController(),
			new TileTickQueueController(),
			new BlockEventQueueController()
	);

	private ContainerManipulator()
	{
		super("container");
	}

	public static ContainerManipulator getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void buildSubCommand(CommandTreeContext.Node context)
	{
		COLLECTION_CONTROLLERS.forEach(controller -> context.node.then(controller.getCommandNode(context)));
	}
}
