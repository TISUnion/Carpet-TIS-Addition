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
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin
{
	@Shadow @Final
	//#if MC < 11700 || MC >= 12105
	private
	//#endif
	ServerWorld world;

	@Inject(
			//#if MC >= 12105
			//$$ method = "tickChunks(Lnet/minecraft/util/profiler/Profiler;J)V",
			//#elseif MC >= 12102
			//$$ method = "tickChunks(Lnet/minecraft/util/profiler/Profiler;JLjava/util/List;)V",
			//#else
			method = "tickChunks",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12109
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;tickSpawners(Z)V"
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;tickSpawners(ZZ)V"
					//#else
					target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;spawnEntities(Lnet/minecraft/server/world/ServerWorld;ZZ)V"
					//#endif
			)
	)
	private void enterStageSpawnSpecial(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.SPAWNING_SPECIAL);
	}

	@Inject(
			//#if MC >= 12105
			//$$ method = "tickChunks(Lnet/minecraft/util/profiler/Profiler;J)V",
			//#elseif MC >= 12102
			//$$ method = "tickChunks(Lnet/minecraft/util/profiler/Profiler;JLjava/util/List;)V",
			//#else
			method = "tickChunks",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 12109
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;tickSpawners(Z)V",
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ServerWorld;tickSpawners(ZZ)V",
					//#else
					target = "Lnet/minecraft/world/gen/chunk/ChunkGenerator;spawnEntities(Lnet/minecraft/server/world/ServerWorld;ZZ)V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void exitStageSpawnSpecial(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage(this.world, TickStage.UNKNOWN);
	}
}
