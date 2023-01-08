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

package carpettisaddition.mixins.logger.ticket;

import carpettisaddition.logging.loggers.ticket.IChunkTicketManager;
import carpettisaddition.logging.loggers.ticket.TicketLogger;
import net.minecraft.server.world.ChunkTicket;
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ChunkTicketManager.class)
public abstract class ChunkTicketManagerMixin implements IChunkTicketManager
{
	private ServerWorld world;

	@Override
	public void setServerWorld(ServerWorld world)
	{
		this.world = world;
	}

	@Inject(
			method = "addTicket(JLnet/minecraft/server/world/ChunkTicket;)V",
			at = @At(value = "HEAD")
	)
	private void onAddTicket(long position, ChunkTicket<?> chunkTicket, CallbackInfo ci)
	{
		TicketLogger.onAddTicket(this.world, position, chunkTicket);
	}

	@Inject(
			method = "removeTicket(JLnet/minecraft/server/world/ChunkTicket;)V",
			at = @At(value = "HEAD")
	)
	private void onRemoveTicket(long position, ChunkTicket<?> chunkTicket, CallbackInfo ci)
	{
		TicketLogger.onRemoveTicket(this.world, position, chunkTicket);
	}
}
