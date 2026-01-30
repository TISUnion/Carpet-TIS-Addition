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
import carpettisaddition.utils.compat.DimensionWrapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

//#if MC >= 11700
//$$ import carpettisaddition.mixins.utils.DirectBlockEntityTickInvokerAccessor;
//$$ import carpettisaddition.mixins.utils.WrappedBlockEntityTickInvokerAccessor;
//$$ import net.minecraft.world.level.block.entity.BlockEntity;
//$$ import net.minecraft.world.level.block.entity.TickingBlockEntity;
//#endif

//#if MC < 11600
import net.minecraft.world.level.dimension.DimensionType;
//#endif

@SuppressWarnings("UnusedReturnValue")
public class GameUtils
{
	public static Level getOverworld(MinecraftServer server)
	{
		//#if MC >= 11600
		//$$ return server.getLevel(Level.OVERWORLD);
		//#else
		return server.getLevel(DimensionType.OVERWORLD);
		//#endif
	}

	public static Level getOverworld()
	{
		return getOverworld(CarpetTISAdditionServer.minecraft_server);
	}

	private static final List<DimensionWrapper> COMMON_DIMENSIONS = ImmutableList.of(DimensionWrapper.OVERWORLD, DimensionWrapper.THE_NETHER, DimensionWrapper.THE_END);

	// list worlds in a nice and consistent order: overworld, the nether, the end
	public static List<ServerLevel> listWorlds(MinecraftServer server)
	{
		List<ServerLevel> levels = Lists.newArrayList();
		for (DimensionWrapper dimension : COMMON_DIMENSIONS)
		{
			ServerLevel level = server.getLevel(dimension.getValue());
			if (level != null)
			{
				levels.add(level);
			}
		}
		for (ServerLevel level : server.getAllLevels())
		{
			if (!COMMON_DIMENSIONS.contains(DimensionWrapper.of(level)))
			{
				levels.add(level);
			}
		}
		return levels;
	}

	public static long getGameTime()
	{
		return WorldUtils.getWorldTime(Objects.requireNonNull(getOverworld()));
	}

	public static boolean isOnServerThread()
	{
		return CarpetTISAdditionServer.minecraft_server != null && CarpetTISAdditionServer.minecraft_server.isSameThread();
	}

	/**
	 * See the exit point for the looping in
	 *   (>=1.16) {@link net.minecraft.world.level.NaturalSpawner#createState}
	 *   (<=1.15) {@link net.minecraft.server.level.ServerLevel#getMobCategoryCounts}
	 */
	public static boolean countsTowardsMobcap(Entity entity)
	{
		if (entity instanceof Mob)
		{
			Mob mobEntity = (Mob)entity;
			return !mobEntity.isPersistenceRequired() && !mobEntity.requiresCustomPersistence();
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
	public static Player getPlayerFromName(String playerName)
	{
		return CarpetTISAdditionServer.minecraft_server.getPlayerList().getPlayerByName(playerName);
	}

	//#if MC >= 11700
	//$$ /**
	//$$  * for mc 1.17+
	//$$  */
	//$$ @Nullable
	//$$ public static BlockEntity getBlockEntityFromTickInvoker(TickingBlockEntity blockEntityTickInvoker)
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
