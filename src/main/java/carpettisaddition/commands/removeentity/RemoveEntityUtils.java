/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.commands.removeentity;

import com.google.common.collect.Queues;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;

public class RemoveEntityUtils
{
	public static int removeEntities(Collection<? extends Entity> entities)
	{
		Queue<Entity> toRemoveQueue = Queues.newArrayDeque();

		entities.stream().
				filter(entity -> !(entity instanceof Player) && EntitySelector.NO_SPECTATORS.test(entity)).
				forEach(toRemoveQueue::add);

		int count = 0;
		while (!toRemoveQueue.isEmpty())
		{
			Entity entity = toRemoveQueue.poll();

			if (entity instanceof EntityToBeCleanlyRemoved)
			{
				((EntityToBeCleanlyRemoved)entity).setToBeCleanlyRemoved$TISCM();
			}
			if (entity instanceof EnderDragon)
			{
				Collections.addAll(toRemoveQueue, ((EnderDragon)entity).getSubEntities());
			}

			//#if MC >= 11700
			//$$ entity.discard();
			//#else
			entity.remove();
			//#endif

			count++;
		}

		return count;
	}
}
