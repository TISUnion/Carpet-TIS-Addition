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

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

//#if MC >= 12102
//$$ import net.minecraft.world.entity.EntityType;
//$$ import net.minecraft.world.entity.EntitySpawnReason;
//#endif

//#if MC >= 12004
//$$ import net.minecraft.server.level.ServerLevel;
//#endif

//#if MC >= 11700
//$$ import net.minecraft.world.level.gameevent.GameEvent;
//#endif

public class MinecartPlaceableOnGroundImpl
{
	/**
	 * Reference: {@link net.minecraft.world.item.MinecartItem#useOn}
	 */
	public static InteractionResult placeMinecartOnGround(
			UseOnContext context,
			//#if MC >= 12102
			//$$ EntityType<? extends AbstractMinecart> minecartType
			//#else
			AbstractMinecart.Type minecartType
			//#endif
	)
	{
		Player player = context.getPlayer();
		Level world = context.getLevel();
		ItemStack itemStack = context.getItemInHand();
		Vec3 vec3d = context.getClickLocation();

		if (
				//#if MC >= 12004
				//$$ world instanceof ServerLevel serverWorld
				//#else
				!world.isClientSide()
				//#endif
		)
		{
			//#if MC >= 12102
			//$$ AbstractMinecart minecartEntity = AbstractMinecart.createMinecart(serverWorld, vec3d.x(), vec3d.y(), vec3d.z(), minecartType, EntitySpawnReason.DISPENSER, itemStack, player);
			//#elseif MC >= 12004
			//$$ AbstractMinecart minecartEntity = AbstractMinecart.createMinecart(serverWorld, vec3d.x(), vec3d.y(), vec3d.z(), minecartType, itemStack, player);
			//#else
			AbstractMinecart minecartEntity = AbstractMinecart.createMinecart(world, vec3d.x(), vec3d.y(), vec3d.z(), minecartType);
			if (itemStack.hasCustomHoverName())
			{
				minecartEntity.setCustomName(itemStack.getHoverName());
			}
			//#endif

			if (player != null)
			{
				//#if MC >= 11700
				//$$ minecartEntity.setYRot(player.getYRot());
				//#else
				minecartEntity.yRot = player.yRot;
				//#endif
			}

			// check if the space of the minecart entity is occupied, like what boat placement does
			if (!isMinecartPositionValid(world, minecartEntity))
			{
				return InteractionResult.FAIL;
			}

			world.addFreshEntity(minecartEntity);
			//#if MC >= 11700
			//$$ world.gameEvent(context.getPlayer(), net.minecraft.world.level.gameevent.GameEvent.ENTITY_PLACE, context.getClickedPos());
			//#endif
		}
		else
		{
			// For proper client swing-hand animation. AbstractMinecart.create() needs ServerLevel in mc1.20.4+, we can't use it on the clientside
			// Assumption: The simply-initialized minecart entity has the same size of the serverside-created one,
			// i.e., no nbt scaling tricks, modded new minecart variant

			//#if MC >= 12102
			//$$ Minecart minecartEntity = new Minecart(minecartType, world);
			//$$ minecartEntity.setPos(vec3d.x(), vec3d.y(), vec3d.z());
			//$$ if (!isMinecartPositionValid(world, minecartEntity))
			//#else
			if (!isMinecartPositionValid(world, new Minecart(world, vec3d.x(), vec3d.y(), vec3d.z())))
			//#endif
			{
				return InteractionResult.FAIL;
			}
		}

		itemStack.shrink(1);

		//#if MC >= 12102
		//$$ return world.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
		//#elseif MC >= 11600
		//$$ return InteractionResult.sidedSuccess(world.isClientSide());
		//#else
		return InteractionResult.SUCCESS;
		//#endif
	}

	private static boolean isMinecartPositionValid(Level world, AbstractMinecart minecartEntity)
	{
		// reference: net.minecraft.item.BoatItem#use
		return world.noCollision(
				minecartEntity,
				minecartEntity.getBoundingBox()
						//#if MC < 11800
						.inflate(-0.1D)
						//#endif
		);
	}
}
