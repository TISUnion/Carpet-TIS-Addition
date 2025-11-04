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

package carpettisaddition.mixins.carpet.tweaks.command.dontSubscribeLoggerForOther;

import carpet.commands.LogCommand;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.PlayerUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LogCommand.class)
public abstract class LogCommandMixin
{
	@Inject(
			method = "subscribePlayer",
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	private static void dontSubscribeLoggerForOther_logSubCommand(CommandSourceStack source, String player_name, String logname, String option, CallbackInfoReturnable<Integer> cir)
	{
		dontSubscribeLoggerForOtherImpl(source, player_name, cir);
	}

	@Inject(
			method = "unsubFromAll",
			at = @At("HEAD"),
			remap = false,
			cancellable = true
	)
	private static void dontSubscribeLoggerForOther_cleanSubCommand(CommandSourceStack source, String player_name, CallbackInfoReturnable<Integer> cir)
	{
		dontSubscribeLoggerForOtherImpl(source, player_name, cir);
	}

	@Unique
	private static void dontSubscribeLoggerForOtherImpl(CommandSourceStack source, String playerName, CallbackInfoReturnable<Integer> cir)
	{
		MinecraftServer server = source.getServer();
		Player playerToControl = server.getPlayerList().getPlayerByName(playerName);

		// source is player and is going to control a valid player
		if (playerToControl != null && source.getEntity() instanceof ServerPlayer)
		{
			ServerPlayer player = (ServerPlayer)source.getEntity();
			// source player is not op, and is not the player-to-control
			if (!player.getUUID().equals(playerToControl.getUUID()) && !PlayerUtils.isOperator(server, player))
			{
				Messenger.tell(source, Messenger.formatting(new Translator("misc").tr("log_subscribe_for_other_permission_denied"), "r"));
				cir.setReturnValue(0);
			}
		}
	}
}
