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

package carpettisaddition.commands.manipulate.container.controller;

import carpettisaddition.mixins.command.manipulate.container.ServerWorldAccessor;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//#if MC >= 11700
//$$ import carpettisaddition.mixins.command.manipulate.container.EntityListAccessor;
//#endif

public class EntityListController extends AbstractEntityListController
{
	public EntityListController()
	{
		super("entity");
	}

	@Override
	protected boolean canManipulate(ServerLevel world)
	{
		//#if MC >= 11700
		//$$ return true;
		//#else
		return !((ServerWorldAccessor)world).isTickingEntity();
		//#endif
	}

	@Override
	protected int processWholeList(ServerLevel world, Consumer<List<?>> collectionOperator)
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
