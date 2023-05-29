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

package carpettisaddition.mixins.logger.ghostPlayer;

import carpettisaddition.logging.loggers.ghostPlayer.GhostPlayerLogger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Inject(method = "removeEntity", at = @At("HEAD"))
	private void ghostPlayerLogger_removeEntityHook(Entity entity, CallbackInfo ci)
	{
		if (entity instanceof PlayerEntity)
		{
			GhostPlayerLogger.getInstance().onWorldRemoveEntity((ServerWorld)(Object)this, (PlayerEntity)entity, "ServerWorld#removeEntity");
		}
	}

	@Inject(method = "unloadEntity", at = @At("HEAD"))
	private void ghostPlayerLogger_unloadEntityHook(Entity entity, CallbackInfo ci)
	{
		if (entity instanceof PlayerEntity)
		{
			GhostPlayerLogger.getInstance().onWorldRemoveEntity((ServerWorld)(Object)this, (PlayerEntity)entity, "ServerWorld#unloadEntity");
		}
	}

	@Inject(method = "removePlayer", at = @At("HEAD"))
	private void ghostPlayerLogger_removePlayerHook(ServerPlayerEntity player, CallbackInfo ci)
	{
		GhostPlayerLogger.getInstance().onWorldRemoveEntity((ServerWorld)(Object)this, player, "ServerWorld#removePlayer");
	}
}
