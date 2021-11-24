package carpettisaddition.commands.manipulate.container;

import carpettisaddition.mixins.command.manipulate.WorldAccessor;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.function.Consumer;

public class TileEntityListController extends AbstractEntityListController
{
	public TileEntityListController()
	{
		super("tile_entity");
	}

	@Override
	protected boolean canManipulate(ServerWorld world)
	{
		return !((WorldAccessor)world).isIteratingTickingBlockEntities();
	}

	@Override
	protected int processWholeList(ServerWorld world, Consumer<List<?>> collectionOperator)
	{
		collectionOperator.accept(world.tickingBlockEntities);
		return world.tickingBlockEntities.size();
	}
}
