package carpettisaddition.commands.manipulate.container;

import carpettisaddition.mixins.command.manipulate.ServerWorldAccessor;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EntityListController extends AbstractEntityListController
{
	public EntityListController()
	{
		super("entity");
	}

	@Override
	protected boolean canManipulate(ServerWorld world)
	{
		return !((ServerWorldAccessor)world).isTickingEntity();
	}

	@Override
	protected int processWholeList(ServerWorld world, Consumer<List<?>> collectionOperator)
	{
		Int2ObjectMap<Entity> map = ((ServerWorldAccessor)world).getEntitiesById();
		List<Pair<Integer, Entity>> list = map.int2ObjectEntrySet().stream().map(entry -> Pair.of(entry.getIntKey(), entry.getValue())).collect(Collectors.toList());
		collectionOperator.accept(list);
		map.clear();
		list.forEach(entityEntry -> map.put((int)entityEntry.getFirst(), entityEntry.getSecond()));
		return list.size();
	}
}
