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
import net.minecraft.util.thread.ThreadExecutor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkThreadUtils.class)
public abstract class NetworkThreadUtilsMixin<T>
{
	/**
	 * Stage reset happens in {@link MinecraftServerMixin}
	 */
	@Inject(
			method = "forceMainThread(Lnet/minecraft/network/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/util/thread/ThreadExecutor;)V",
			at = @At("HEAD")
	)
	private static <T extends PacketListener> void startProcessPacket(Packet<T> packet, T listener, ThreadExecutor<?> engine, CallbackInfo ci)
	{
		if (engine.isOnThread())
		{
			if (listener instanceof ServerPlayNetworkHandler)
			{
				ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler) listener;
				MicroTimingLoggerManager.setTickStage(TickStage.PLAYER_ACTION);
				MicroTimingLoggerManager.setSubTickStage(new PlayerActionSubStage(handler.player));
			}
		}
	}
}
