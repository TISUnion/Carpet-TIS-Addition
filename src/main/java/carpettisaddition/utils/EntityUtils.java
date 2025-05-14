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

package carpettisaddition.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//#if MC >= 11700
//$$ import carpettisaddition.mixins.utils.ExperienceOrbEntityAccessor;
//#endif

public class EntityUtils
{
	public static World getEntityWorld(@NotNull Entity entity)
	{
		//#if MC >= 12106
		//$$ return entity.getWorld();
		//#else
		return entity.getEntityWorld();
		//#endif
	}

	public static PlayerAbilities getPlayerAbilities(@NotNull PlayerEntity player)
	{
		//#if MC >= 11700
		//$$ return player.getAbilities();
		//#else
		return player.abilities;
		//#endif
	}

	public static boolean isCreativePlayer(@Nullable Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)entity;
			return player.isCreative();
		}
		return false;
	}

	public static boolean isFlyingCreativePlayer(@Nullable Entity entity)
	{
		if (entity instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity)entity;
			return isCreativePlayer(entity) && getPlayerAbilities(player).flying;
		}
		return false;
	}

	public static float getYaw(@NotNull Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getYaw();
		//#else
		return entity.yaw;
		//#endif
	}

	public static float getPitch(@NotNull Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getPitch();
		//#else
		return entity.pitch;
		//#endif
	}

	@Nullable
	public static GameMode getPlayerGameMode(PlayerEntity player)
	{
		if (player instanceof ServerPlayerEntity)
		{
			return ((ServerPlayerEntity)player).interactionManager.getGameMode();
		}
		else if (player instanceof AbstractClientPlayerEntity)
		{
			return Optional.ofNullable(MinecraftClient.getInstance().getNetworkHandler())
					.map(h -> h.getPlayerListEntry(player.getGameProfile().getId()))
					.map(PlayerListEntry::getGameMode)
					.orElse(null);
		}
		else
		{
			return null;
		}
	}

	public static int getXpOrbPickingCount(ExperienceOrbEntity experienceOrbEntity)
	{
		return
				//#if MC >= 11700
				//$$ ((ExperienceOrbEntityAccessor)experienceOrbEntity).getPickingCount();
				//#else
				1;
				//#endif
	}
}
