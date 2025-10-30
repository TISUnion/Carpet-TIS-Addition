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

import carpet.commands.LogCommand;
import carpet.logging.Logger;
import carpet.logging.LoggerRegistry;
import carpettisaddition.helpers.carpet.loggerRestriction.CarpetLoggerRestriction;
import carpettisaddition.helpers.carpet.loggerRestriction.RestrictionCheckResult;
import carpettisaddition.utils.Messenger;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Objects;

@Mixin(LogCommand.class)
public abstract class LogCommandMixin
{
	@Inject(
			method = "toggleSubscription",
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/logging/LoggerRegistry;togglePlayerSubscription(Ljava/lang/String;Ljava/lang/String;)Z",
					remap = false
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private static void switchableLogger_toggleCommand(CommandSourceStack source, String player_name, String logName, CallbackInfoReturnable<Integer> cir, Player player)
	{
		checkLoggerRestriction(source, LoggerRegistry.getLogger(logName), player, null, cir);
	}

	@Inject(
			method = "subscribePlayer",
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/logging/LoggerRegistry;subscribePlayer(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
					remap = false
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			cancellable = true
	)
	private static void switchableLogger_subscribeCommand(CommandSourceStack source, String player_name, String logName, String option, CallbackInfoReturnable<Integer> cir, Player player)
	{
		checkLoggerRestriction(source, LoggerRegistry.getLogger(logName), player, option, cir);
	}

	private static void checkLoggerRestriction(CommandSourceStack source, Logger logger, Player player, String option, CallbackInfoReturnable<Integer> cir)
	{
		if (logger == null)
		{
			// just in case
			return;
		}
		RestrictionCheckResult result = CarpetLoggerRestriction.checkLoggerSubscribable(logger, player, option);
		if (!result.isPassed())
		{
			Messenger.tell(source, Objects.requireNonNull(result.getErrorMessage()));
			cir.setReturnValue(0);
		}
	}
}
