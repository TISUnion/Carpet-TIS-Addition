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
	//#if MC < 11700
	private
	//#endif
	ServerWorld world;

	@Inject(
			method = "tickChunks",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11800
					//$$ target = "Lnet/minecraft/server/world/ChunkTicketManager;getTickedChunkCount()I"
					//#elseif MC >= 11600
					//$$ target = "Lnet/minecraft/server/world/ChunkTicketManager;getSpawningChunkCount()I"
					//#else
					target = "Lnet/minecraft/server/world/ServerWorld;getMobCountsByCategory()Lit/unimi/dsi/fastutil/objects/Object2IntMap;"
					//#endif
			)
	)
	private void onCountingMobcapLifeTimeTracker(CallbackInfo ci)
	{
		((ServerWorldWithLifeTimeTracker)this.world).getLifeTimeWorldTracker().increaseSpawnStageCounter();
	}
}
