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

package carpettisaddition.mixins.carpet.tweaks.loggerRestriction;

import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionServer;
import carpettisaddition.helpers.carpet.loggerRestriction.CarpetLoggerRestriction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = LoggerRegistry.class, priority = 500)
public abstract class LoggerRegistryMixin
{
	@Inject(
			method = "subscribePlayer",
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	private static void switchableLogger_lastCheck(String playerName, String logName, String option, CallbackInfo ci)
	{
		Logger logger = LoggerRegistry.getLogger(logName);
		Optional<ServerPlayer> opt = Optional.ofNullable(CarpetTISAdditionServer.minecraft_server).
				map(MinecraftServer::getPlayerList).
				map(pm -> pm.getPlayerByName(playerName));
		if (logger != null && opt.isPresent())
		{
			if (!CarpetLoggerRestriction.isLoggerSubscribable(logger, opt.get(), option))
			{
				CarpetTISAdditionServer.LOGGER.warn("Logger {} option {} is not subscribable for player {}", logger.getLogName(), option, playerName);
				ci.cancel();
			}
		}
	}
}
