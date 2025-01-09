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
import net.minecraft.server.world.ChunkTicketManager;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 12105
//$$ import net.minecraft.class_10592;
//#endif

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManagerMixin
{
	//#if MC >= 12105
	//$$ @Shadow @Final private class_10592 field_55594;
	//#else
	@Shadow @Final private ChunkTicketManager ticketManager;
	//#endif

	@Shadow @Final
	//#if MC < 11700 || MC >= 12103
	private
	//#endif
	ServerWorld world;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void ticketLogger_attachServerWorldToTicketManager(CallbackInfo ci)
	{
		//#if MC >= 12105
		//$$ ((TicketManagerWithServerWorld)this.field_55594).setServerWorld$TISCM(this.world);
		//#else
		((TicketManagerWithServerWorld)this.ticketManager).setServerWorld$TISCM(this.world);
		//#endif
	}
}
