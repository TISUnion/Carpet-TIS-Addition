/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.minecartPlaceableOnGround;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

//#if MC >= 12004
//$$ import net.minecraft.server.world.ServerWorld;
//#endif

//#if MC >= 11700
//$$ import net.minecraft.world.event.GameEvent;
//#endif

public class MinecartPlaceableOnGroundImpl
{
	/**
	 * Reference: {@link net.minecraft.item.MinecartItem#useOnBlock}
	 */
	public static ActionResult placeMinecartOnGround(ItemUsageContext context, AbstractMinecartEntity.Type minecartType)
	{
		PlayerEntity player = context.getPlayer();
		World world = context.getWorld();
		ItemStack itemStack = context.getStack();
		Vec3d vec3d = context.getHitPos();

		if (
				//#if MC >= 12004
				//$$ world instanceof ServerWorld serverWorld
				//#else
				!world.isClient
				//#endif
		)
		{
			//#if MC >= 12004
			//$$ AbstractMinecartEntity minecartEntity = AbstractMinecartEntity.create(serverWorld, vec3d.getX(), vec3d.getY(), vec3d.getZ(), minecartType, itemStack, player);
			//#else
			AbstractMinecartEntity minecartEntity = AbstractMinecartEntity.create(world, vec3d.getX(), vec3d.getY(), vec3d.getZ(), minecartType);
			if (itemStack.hasCustomName())
			{
				minecartEntity.setCustomName(itemStack.getName());
			}
			//#endif

			if (player != null)
			{
				//#if MC >= 11700
				//$$ minecartEntity.setYaw(player.getYaw());
				//#else
				minecartEntity.yaw = player.yaw;
				//#endif
			}

			// check if the space of the minecart entity is occupied, like what boat placement does
			if (!isMinecartPositionValid(world, minecartEntity))
			{
				return ActionResult.FAIL;
			}

			world.spawnEntity(minecartEntity);
			//#if MC >= 11700
			//$$ world.emitGameEvent(context.getPlayer(), net.minecraft.world.event.GameEvent.ENTITY_PLACE, context.getBlockPos());
			//#endif
		}
		else
		{
			// For proper client swing-hand animation. AbstractMinecartEntity.create() needs ServerWorld in mc1.20.4+, we can't use it on the clientside
			// Assumption: The simply-initialized minecart entity has the same size of the serverside-created one,
			// i.e., no nbt scaling tricks, modded new minecart variant
			if (!isMinecartPositionValid(world, new MinecartEntity(world, vec3d.getX(), vec3d.getY(), vec3d.getZ())))
			{
				return ActionResult.FAIL;
			}
		}

		itemStack.decrement(1);

		//#if MC >= 12102
		//$$ return world.isClient ? ActionResult.SUCCESS : ActionResult.SUCCESS_SERVER;
		//#elseif MC >= 11600
		//$$ return ActionResult.success(world.isClient);
		//#else
		return ActionResult.SUCCESS;
		//#endif
	}

	private static boolean isMinecartPositionValid(World world, AbstractMinecartEntity minecartEntity)
	{
		// reference: net.minecraft.item.BoatItem#use
		return world.doesNotCollide(
				minecartEntity,
				minecartEntity.getBoundingBox()
						//#if MC < 11800
						.expand(-0.1D)
						//#endif
		);
	}
}
