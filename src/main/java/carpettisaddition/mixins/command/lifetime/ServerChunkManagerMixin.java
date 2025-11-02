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

package carpettisaddition.mixins.command.lifetime;

import carpettisaddition.commands.lifetime.interfaces.ServerWorldWithLifeTimeTracker;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkManagerMixin
{
	@Shadow @Final
	//#if MC < 11700 || MC >= 12105
	private
	//#endif
	ServerLevel level;

	@Inject(
			//#if MC >= 12105
			//$$ method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;J)V",
			//#elseif MC >= 12102
			//$$ method = "tickChunks(Lnet/minecraft/util/profiling/ProfilerFiller;JLjava/util/List;)V",
			//#else
			method = "tickChunks",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/world/level/NaturalSpawner;createState(ILjava/lang/Iterable;Lnet/minecraft/world/level/NaturalSpawner$ChunkGetter;Lnet/minecraft/world/level/LocalMobCapCalculator;)Lnet/minecraft/world/level/NaturalSpawner$SpawnState;"
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/world/level/NaturalSpawner;createState(ILjava/lang/Iterable;Lnet/minecraft/world/level/NaturalSpawner$ChunkGetter;)Lnet/minecraft/world/level/NaturalSpawner$SpawnState;"
					//#else
					target = "Lnet/minecraft/server/level/ServerLevel;getMobCategoryCounts()Lit/unimi/dsi/fastutil/objects/Object2IntMap;"
					//#endif
			)
	)
	private void onCountingMobcapLifeTimeTracker(CallbackInfo ci)
	{
		((ServerWorldWithLifeTimeTracker)this.level).getLifeTimeWorldTracker().increaseSpawnStageCounter();
	}
}
