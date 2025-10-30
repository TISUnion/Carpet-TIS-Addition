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

package carpettisaddition.helpers.carpet.tweaks.logger.projectile;

import carpettisaddition.utils.Messenger;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

//#if MC >= 12102
//$$ import net.minecraft.item.ItemStack;
//$$ import net.minecraft.item.Items;
//#endif

public class VisualizeTrajectoryHelper
{
	public static final String TISCM_VISPROJ_LOGGER = "##TISCM_VISPROJ_LOGGER##";
	public static final Map<UUID, Entity> VISUALIZE_SNOWBALLS = Maps.newHashMap();

	public static boolean isVisualizeProjectile(@Nullable Entity entity)
	{
		return entity instanceof Snowball && entity.getTags().contains(TISCM_VISPROJ_LOGGER);
	}

	public static void clearVisualizers()
	{
		// might trigger clearVisualizers recursively during entity removing
		// so make a copy and clean the list first
		List<Entity> snowballEntities = Lists.newArrayList(VISUALIZE_SNOWBALLS.values());
		VISUALIZE_SNOWBALLS.clear();
		snowballEntities.forEach(
				//#if MC >= 11700
				//$$ Entity::discard
				//#else
				Entity::remove
				//#endif
		);
	}

	public static void addVisualizer(Entity entity)
	{
		VISUALIZE_SNOWBALLS.put(entity.getUUID(), entity);
	}

	public static void createVisualizer(Level world, Vec3 pos, String name)
	{
		TrajectoryLoggerUtil.isVisualizer.set(true);
		Snowball snowball = new Snowball(
				world, pos.x(), pos.y(), pos.z()
				//#if MC >= 12102
				//$$ , new ItemStack(Items.SNOWBALL)
				//#endif
		);
		snowball.setNoGravity(true);
		snowball.setCustomName(Messenger.s(name));
		snowball.setCustomNameVisible(true);
		snowball.addTag(TISCM_VISPROJ_LOGGER);
		if (world.addFreshEntity(snowball))
		{
			addVisualizer(snowball);
		}
	}

	public static void onVisualizerReacted(ServerPlayer player, Entity snowBall)
	{
		Messenger.tell(player, Messenger.s(snowBall.position().toString()));
	}
}
