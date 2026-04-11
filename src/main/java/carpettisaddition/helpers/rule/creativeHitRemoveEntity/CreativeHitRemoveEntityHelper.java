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

package carpettisaddition.helpers.rule.creativeHitRemoveEntity;

import carpettisaddition.commands.removeentity.RemoveEntityUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

//#if MC >= 1.21.5
//$$ import net.minecraft.tags.ItemTags;
//#else
import net.minecraft.world.item.SwordItem;
//#endif

public class CreativeHitRemoveEntityHelper
{
	public static void removeEntityHit(ServerPlayer player, Entity victim)
	{
		ServerLevel level = player.getLevel();
		List<Entity> toRemove = Lists.newArrayList();
		toRemove.add(victim);

		SoundEvent soundEvent;
		if (simpleSweepingCheck(player))
		{
			for (Entity entity : level.getEntitiesOfClass(Entity.class, victim.getBoundingBox().inflate(1.0, 0.25, 1.0)))
			{
				if (entity != player && entity != victim && RemoveEntityUtils.canRemove(entity) && player.distanceToSqr(entity) < 9.0)
				{
					toRemove.add(entity);
				}
			}
			soundEvent = SoundEvents.PLAYER_ATTACK_SWEEP;
		}
		else
		{
			soundEvent = SoundEvents.PLAYER_ATTACK_STRONG;
		}

		RemoveEntityUtils.removeEntities(toRemove);

		// reference: net.minecraft.world.entity.player.Player#attack
		// disable remap to prevent it from mapping `playSound` to `playSeededSound` from 1.18 to 1.19
		//#disable-remap
		level.playSound
		//#enable-remap
		(
				null,
				//#if MC >= 11500
				player.getX(), player.getY(), player.getZ(),
				//#else
				//$$ player.x, player.y, player.z,
				//#endif
				soundEvent,
				player.getSoundSource(),
				0.7F,  // volume
				0.9F  // pitch
		);
	}

	private static boolean simpleSweepingCheck(ServerPlayer player)
	{
		//#if MC >= 1.21.5
		//$$ boolean holdingSword = player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemTags.SWORDS);
		//#else
		boolean holdingSword = player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof SwordItem;
		//#endif

		//#if MC >= 1.16
		//$$ boolean onGround = player.isOnGround();
		//#else
		boolean onGround = player.onGround;
		//#endif

		return holdingSword && onGround && !player.isSprinting();
	}
}
