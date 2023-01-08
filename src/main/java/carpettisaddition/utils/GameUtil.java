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

import carpettisaddition.CarpetTISAdditionServer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


//#if MC >= 11700
//$$ import carpettisaddition.mixins.utils.DirectBlockEntityTickInvokerAccessor;
//$$ import carpettisaddition.mixins.utils.WrappedBlockEntityTickInvokerAccessor;
//$$ import net.minecraft.block.entity.BlockEntity;
//$$ import net.minecraft.world.chunk.BlockEntityTickInvoker;
//#endif

//#if MC < 11600
import net.minecraft.world.dimension.DimensionType;
//#endif

public class GameUtil
{
	public static long getGameTime()
	{
		//#if MC >= 11600
		//$$ World world = CarpetTISAdditionServer.minecraft_server.getWorld(World.OVERWORLD);
		//#else
		World world = CarpetTISAdditionServer.minecraft_server.getWorld(DimensionType.OVERWORLD);
		//#endif

		return Objects.requireNonNull(world).getTime();
	}

	public static boolean isOnServerThread()
	{
		return CarpetTISAdditionServer.minecraft_server != null && CarpetTISAdditionServer.minecraft_server.isOnThread();
	}

	/**
	 * See the exit point for the looping in
	 *   (>=1.16) {@link net.minecraft.world.SpawnHelper#setupSpawn}
	 *   (<=1.15) {@link net.minecraft.server.world.ServerWorld#getMobCountsByCategory}
	 */
	public static boolean countsTowardsMobcap(Entity entity)
	{
		if (entity instanceof MobEntity)
		{
			MobEntity mobEntity = (MobEntity)entity;
			return !mobEntity.isPersistent() && !mobEntity.cannotDespawn();
		}
		return false;
	}

	/**
	 * Return a BlockPos that is out of the world limit
	 */
	public static BlockPos getInvalidBlockPos()
	{
		return new BlockPos(0, -1024, 0);
	}

	@Nullable
	public static PlayerEntity getPlayerFromName(String playerName)
	{
		return CarpetTISAdditionServer.minecraft_server.getPlayerManager().getPlayer(playerName);
	}

	//#if MC >= 11700
	//$$ /**
	//$$  * for mc 1.17+
	//$$  */
	//$$ @Nullable
	//$$ public static BlockEntity getBlockEntityFromTickInvoker(BlockEntityTickInvoker blockEntityTickInvoker)
	//$$ {
	//$$ 	if (blockEntityTickInvoker instanceof DirectBlockEntityTickInvokerAccessor)
	//$$ 	{
	//$$ 		return ((DirectBlockEntityTickInvokerAccessor<?>) blockEntityTickInvoker).getBlockEntity();
	//$$ 	}
	//$$ 	else if (blockEntityTickInvoker instanceof WrappedBlockEntityTickInvokerAccessor)
	//$$ 	{
	//$$ 		return getBlockEntityFromTickInvoker(((WrappedBlockEntityTickInvokerAccessor<?>)blockEntityTickInvoker).getWrapped());
	//$$ 	}
	//$$ 	return null;
	//$$ }
	//#endif
}
