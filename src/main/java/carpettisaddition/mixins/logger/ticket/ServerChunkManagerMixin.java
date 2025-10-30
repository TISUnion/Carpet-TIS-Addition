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

import carpettisaddition.logging.loggers.ticket.TicketManagerWithServerWorld;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// don't remap ChunkTicketManager to ChunkLevelManager in mc1.21.5+
import net.minecraft.server.level.DistanceManager;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkManagerMixin
{
	//#disable-remap
	@Shadow @Final private DistanceManager distanceManager;
	//#enable-remap

	@Shadow @Final
	//#if MC < 11700 || MC >= 12103
	private
	//#endif
	ServerLevel level;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void ticketLogger_attachServerWorldToTicketManager(CallbackInfo ci)
	{
		//#disable-remap
		((TicketManagerWithServerWorld)this.distanceManager).setServerWorld$TISCM(this.level);
		//#enable-remap
	}
}
