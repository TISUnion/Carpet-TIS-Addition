/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.tickFreezeDeepCommand;

import carpettisaddition.helpers.rule.tickFreezeDeepCommand.DeepFreezableServerTickRateManager;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 1.21.8
//$$ import net.minecraft.server.level.ChunkMap;
//#endif

//#if MC >= 1.21.5
//$$ import net.minecraft.world.level.TicketStorage;
//#else
import net.minecraft.server.level.DistanceManager;
//#endif

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin
{
	@Shadow @Final
	//#if MC >= 1.21
	//$$ private
	//#endif
	ServerLevel level;

	// ref: https://github.com/gnembon/fabric-carpet/blob/72d9dd23e36fe5f71c44b5f098e79d5a58f589e8/src/main/java/carpet/mixins/ServerChunkCache_tickMixin.java#L102-L113
	@WrapWithCondition(
			method = "tick",
			at = @At(
					value = "INVOKE",
					//#if MC >= 1.21.8
					//$$ target = "Lnet/minecraft/world/level/TicketStorage;purgeStaleTickets(Lnet/minecraft/server/level/ChunkMap;)V"
					//#elseif MC >= 1.21.5
					//$$ target = "Lnet/minecraft/world/level/TicketStorage;purgeStaleTickets()V"
					//#else
					target = "Lnet/minecraft/server/level/DistanceManager;purgeStaleTickets()V"
					//#endif
			)
	)
	private boolean tickFreezeDeepCommand_applyTheDeepFreeze_ticketExpirationTicking(
			//#if MC >= 1.21.8
			//$$ TicketStorage instance, ChunkMap map
			//#elseif MC >= 1.21.5
			//$$ TicketStorage instance
			//#else
			DistanceManager instance
			//#endif
	)
	{
		ServerTickRateManager serverTickRateManager = this.level.getServer().tickRateManager();
		if (serverTickRateManager instanceof DeepFreezableServerTickRateManager)
		{
			return !((DeepFreezableServerTickRateManager)serverTickRateManager).isDeeplyFrozen$TISCM();
		}
		return true;
	}
}
