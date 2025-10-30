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

package carpettisaddition.mixins.logger.microtiming.tickstages.spawning;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 11500
import net.minecraft.server.level.ServerLevel;
//#else
//$$ import net.minecraft.world.World;
//#endif

@Mixin(NaturalSpawner.class)
public abstract class SpawnHelperMixin
{
	@ModifyVariable(
			//#if MC >= 11600
			//$$ method = "spawn",
			//#else
			method = "spawnCategoryForChunk",
			//#endif
			at = @At("HEAD"), argsOnly = true
	)
	//#if MC >= 11500
	private static ServerLevel enterStageSpawn(ServerLevel world)
	//#else
	//$$ private static World enterStageSpawn(World world)
	//#endif
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.SPAWNING);
		return world;
	}

	@ModifyVariable(
			//#if MC >= 11600
			//$$ method = "spawn",
			//#else
			method = "spawnCategoryForChunk",
			//#endif
			at = @At("TAIL"),
			argsOnly = true
	)
	//#if MC >= 11500
	private static ServerLevel exitStageSpawn(ServerLevel world)
	//#else
	//$$ private static World exitStageSpawn(World world)
	//#endif
	{
		MicroTimingLoggerManager.setTickStage(world, TickStage.UNKNOWN);
		return world;
	}
}
