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

package carpettisaddition.commands.stop;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.CommandUtils;
import carpettisaddition.utils.Messenger;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class StopCommandDoubleConfirmation
{
	private static final Object LOCK = new Object();
	private static final long CONFIRM_WAIT_DURATION_MS = 60 * 1000;  // 60s
	private static long previousExecuteMs = -1;

	public static void noop()
	{
		// load the class in advanced to prevent NoClassDefFoundError if "/stop" the sever after replacing the mod.jar
	}

	public static void handleDoubleConfirmation(CommandContext<ServerCommandSource> commandContext, CallbackInfoReturnable<Integer> cir)
	{
		if (!CarpetTISAdditionSettings.stopCommandDoubleConfirmation)
		{
			return;
		}

		// only apply this double command on player
		if (!CommandUtils.isPlayerCommandSource(commandContext.getSource()))
		{
			return;
		}

		long currentTimeMs = System.currentTimeMillis();
		synchronized (LOCK)
		{
			if (previousExecuteMs > 0 && currentTimeMs - previousExecuteMs <= CONFIRM_WAIT_DURATION_MS)
			{
				// double confirmed, do the /stop
				return;
			}

			// 1st time or confirmation timeout
			previousExecuteMs = currentTimeMs;
		}

		Messenger.tell(commandContext.getSource(), Messenger.fancy(
				Messenger.tr("carpettisaddition.command.stop.double_confirmation.message"),
				Messenger.tr("carpettisaddition.command.stop.double_confirmation.hover_hint"),
				Messenger.ClickEvents.suggestCommand("/carpet stopCommandDoubleConfirmation")
		), true);
		cir.setReturnValue(0);
	}
}
