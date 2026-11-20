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

package carpettisaddition.helpers.rule.minecartPlaceableOnGround;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

/**
 * mc >= 26.4: subproject 26.4                   <--------
 * mc <  26.4: subproject 1.15.2 (main project)
 *
 * {@link AbstractMinecart#createMinecart} resolves the cart from the stack on both sides.
 */
public class MinecartPlaceableOnGroundImpl
{
	public static InteractionResult placeMinecartOnGround(UseOnContext context)
	{
		Level level = context.getLevel();
		Player player = context.getPlayer();
		ItemStack stack = context.getItemInHand();
		Vec3 pos = context.getClickLocation();
		AbstractMinecart cart = AbstractMinecart.createMinecart(level, pos.x(), pos.y(), pos.z(), EntitySpawnReason.DISPENSER, stack, player);
		if (cart == null)
		{
			return InteractionResult.FAIL;
		}

		if (player != null)
		{
			cart.setYRot(player.getYRot());
		}
		if (!isMinecartPositionValid(level, cart))
		{
			return InteractionResult.FAIL;
		}

		if (level instanceof ServerLevel)
		{
			level.addFreshEntity(cart);
			level.gameEvent(player, GameEvent.ENTITY_PLACE, context.getClickedPos());
		}
		stack.shrink(1);
		return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.SUCCESS_SERVER;
	}

	private static boolean isMinecartPositionValid(Level level, AbstractMinecart cart)
	{
		return level.noCollision(cart, cart.getBoundingBox());
	}
}
