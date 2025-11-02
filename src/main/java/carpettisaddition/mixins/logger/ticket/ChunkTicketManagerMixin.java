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

import carpettisaddition.logging.loggers.ticket.TicketLogger;
import carpettisaddition.logging.loggers.ticket.TicketManagerWithServerWorld;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.Ticket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 1.21.5
//$$ import net.minecraft.world.level.TicketStorage;
//#else
import net.minecraft.server.level.DistanceManager;
//#endif

@Mixin(
		//#if MC >= 1.21.5
		//$$ TicketStorage.class
		//#else
		DistanceManager.class
		//#endif
)
public abstract class ChunkTicketManagerMixin implements TicketManagerWithServerWorld
{
	@Unique
	private ServerLevel world$TISCM = null;

	@Override
	public void setServerWorld$TISCM(ServerLevel world)
	{
		this.world$TISCM = world;
	}

	@SuppressWarnings("rawtypes")  // ChunkTicket is no longer a generic in mc1.21.5+
	@ModifyVariable(
			//#if MC >= 12105
			//$$ method = "addTicket(JLnet/minecraft/server/level/Ticket;)Z",
			//#else
			method = "addTicket(JLnet/minecraft/server/level/Ticket;)V",
			//#endif
			at = @At("HEAD"),
			argsOnly = true
	)
	private long ticketLogger_onAddTicket(long position, @Local(argsOnly = true) Ticket chunkTicket)
	{
		if (this.world$TISCM != null)
		{
			TicketLogger.onAddTicket(this.world$TISCM, position, chunkTicket);
		}
		return position;
	}

	@SuppressWarnings("rawtypes")  // ChunkTicket is no longer a generic in mc1.21.5+
	@ModifyVariable(
			//#if MC >= 12105
			//$$ method = "removeTicket(JLnet/minecraft/server/level/Ticket;)Z",
			//#else
			method = "removeTicket(JLnet/minecraft/server/level/Ticket;)V",
			//#endif
			at = @At("HEAD"),
			argsOnly = true
	)
	private long ticketLogger_onRemoveTicket(long position, @Local(argsOnly = true) Ticket chunkTicket)
	{
		if (this.world$TISCM != null)
		{
			TicketLogger.onRemoveTicket(this.world$TISCM, position, chunkTicket);
		}
		return position;
	}
}
