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
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin
{
	@Inject(method = "addEntity", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void ghostPlayerLogger_addHook(Entity entity, CallbackInfo ci, int k)
	{
		if (entity instanceof PlayerEntity)
		{
			GhostPlayerLogger.getInstance().onChunkSectionAddOrRemovePlayer((WorldChunk)(Object)this, (PlayerEntity)entity, k, "add");
		}
	}

	@Inject(method = "remove(Lnet/minecraft/entity/Entity;I)V", at = @At("TAIL"))
	private void ghostPlayerLogger_removeHook(Entity entity, int i, CallbackInfo ci)
	{
		if (entity instanceof PlayerEntity)
		{
			GhostPlayerLogger.getInstance().onChunkSectionAddOrRemovePlayer((WorldChunk)(Object)this, (PlayerEntity)entity, i, "remove");
		}
	}
}
