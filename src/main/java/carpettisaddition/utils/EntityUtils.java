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

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//#if MC >= 11700
//$$ import carpettisaddition.mixins.utils.ExperienceOrbEntityAccessor;
//#endif

public class EntityUtils
{
	public static Level getEntityWorld(@NotNull Entity entity)
	{
		//#if 1.21.6 <= MC && MC < 1.21.9
		//$$ return entity.getWorld();
		//#else
		return entity.getCommandSenderWorld();
		//#endif
	}

	public static Abilities getPlayerAbilities(@NotNull Player player)
	{
		//#if MC >= 11700
		//$$ return player.getAbilities();
		//#else
		return player.abilities;
		//#endif
	}

	public static boolean isCreativePlayer(@Nullable Entity entity)
	{
		if (entity instanceof Player)
		{
			Player player = (Player)entity;
			return player.isCreative();
		}
		return false;
	}

	public static boolean isFlyingCreativePlayer(@Nullable Entity entity)
	{
		if (entity instanceof Player)
		{
			Player player = (Player)entity;
			return isCreativePlayer(entity) && getPlayerAbilities(player).flying;
		}
		return false;
	}

	public static float getYaw(@NotNull Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getYRot();
		//#else
		return entity.yRot;
		//#endif
	}

	public static float getPitch(@NotNull Entity entity)
	{
		//#if MC >= 11700
		//$$ return entity.getXRot();
		//#else
		return entity.xRot;
		//#endif
	}

	@Nullable
	public static GameType getPlayerGameMode(Player player)
	{
		if (player instanceof ServerPlayer)
		{
			return ((ServerPlayer)player).gameMode.getGameModeForPlayer();
		}
		else if (player instanceof AbstractClientPlayer)
		{
			return Optional.ofNullable(Minecraft.getInstance().getConnection())
					.map(h -> h.getPlayerInfo(player.getGameProfile().getId()))
					.map(PlayerInfo::getGameMode)
					.orElse(null);
		}
		else
		{
			return null;
		}
	}

	public static int getXpOrbPickingCount(ExperienceOrb experienceOrbEntity)
	{
		return
				//#if MC >= 11700
				//$$ ((ExperienceOrbEntityAccessor)experienceOrbEntity).getPickingCount();
				//#else
				1;
				//#endif
	}
}
