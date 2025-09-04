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

package carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.playeraction;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.PlayerActionSubStage;
import carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.MinecraftServerMixin;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 1.21.9
//$$ import net.minecraft.network.PacketApplyBatcher;
//#else
import net.minecraft.util.thread.ThreadExecutor;
//#endif

@Mixin(NetworkThreadUtils.class)
public abstract class NetworkThreadUtilsMixin<T>
{
	/**
	 * Stage reset happens in {@link MinecraftServerMixin}
	 */
	@Inject(
			//#if MC >= 1.21.9
			//$$ method = "forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/network/PacketApplyBatcher;)V",
			//#else
			method = "forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V",
			//#endif
			at = @At("HEAD")
	)
	private static <T extends PacketListener> void startProcessPacket(
			Packet<T> packet, T listener,
			//#if MC >= 1.21.9
			//$$ PacketApplyBatcher engine,
			//#else
			ThreadExecutor<?> engine,
			//#endif
			CallbackInfo ci
	)
	{
		if (engine.isOnThread())
		{
			if (listener instanceof ServerPlayNetworkHandler)
			{
				// reset at carpettisaddition.mixins.logger.microtiming.tickstages.asynctask.MinecraftServerMixin
				ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler) listener;
				MicroTimingLoggerManager.setTickStage(TickStage.PLAYER_ACTION);
				MicroTimingLoggerManager.setSubTickStage(new PlayerActionSubStage(handler.player));
			}
		}
	}
}
