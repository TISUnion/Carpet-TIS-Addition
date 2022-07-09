package carpettisaddition.commands.manipulate.container;

import carpettisaddition.mixins.command.manipulate.ServerWorldAccessor;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//#if MC >= 11700
//$$ import carpettisaddition.mixins.command.manipulate.EntityListAccessor;
//#endif

public class EntityListController extends AbstractEntityListController
{
	public EntityListController()
	{
		super("entity");
	}

	@Override
	protected boolean canManipulate(ServerWorld world)
	{
		//#if MC >= 11700
		//$$ return true;
		//#else
		return !((ServerWorldAccessor)world).isTickingEntity();
		//#endif
	}

	@Override
	protected int processWholeList(ServerWorld world, Consumer<List<?>> collectionOperator)
	{
		//#if MC >= 11700
		//$$ EntityListAccessor entityList = (EntityListAccessor)((ServerWorldAccessor)world).getEntityList();
		//$$ entityList.invokeEnsureSafe();
		//$$ Int2ObjectMap<Entity> map = entityList.getEntities();
		//#else
		Int2ObjectMap<Entity> map = ((ServerWorldAccessor)world).getEntitiesById();
		//#endif

		List<Pair<Integer, Entity>> list = map.int2ObjectEntrySet().stream().map(entry -> Pair.of(entry.getIntKey(), entry.getValue())).collect(Collectors.toList());
		collectionOperator.accept(list);
		map.clear();
		list.forEach(entityEntry -> map.put((int)entityEntry.getFirst(), entityEntry.getSecond()));
		return list.size();
	}
}
