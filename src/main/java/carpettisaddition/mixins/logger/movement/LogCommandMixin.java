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

package carpettisaddition.mixins.logger.movement;

import carpet.commands.LogCommand;
import carpet.logging.LoggerRegistry;
import carpettisaddition.logging.loggers.movement.MovementLogger;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LogCommand.class)
public abstract class LogCommandMixin
{
	@Inject(method = "subscribePlayer", at = @At("RETURN"), remap = false)
	private static void warnUnsupportedMovementShapes(CommandSourceStack source, String playerName, String logName, String option, CallbackInfoReturnable<Integer> cir)
	{
		if (cir.getReturnValueI() > 0 && MovementLogger.NAME.equals(logName))
		{
			ServerPlayer player = source.getServer().getPlayerList().getPlayerByName(playerName);
			Map<String, String> subscriptions = LoggerRegistry.getPlayerSubscriptions(playerName);
			if (player != null && subscriptions != null && subscriptions.containsKey(logName))
			{
				MovementLogger.getInstance().warnUnsupportedShapes(player, subscriptions.get(logName));
			}
		}
	}
}

