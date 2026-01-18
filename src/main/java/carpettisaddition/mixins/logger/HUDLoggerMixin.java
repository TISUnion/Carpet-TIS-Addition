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

package carpettisaddition.mixins.logger;

import carpet.logging.HUDLogger;
import carpet.logging.Logger;
import carpettisaddition.logging.loggers.HUDLoggerButCanLogToChat;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;

//#if MC >= 1.19
//$$ import net.minecraft.network.chat.Component;
//#else
import net.minecraft.network.chat.BaseComponent;
//#endif

@Mixin(HUDLogger.class)
public abstract class HUDLoggerMixin extends Logger implements HUDLoggerButCanLogToChat
{
	public HUDLoggerMixin(
			Field acceleratorField, String logName, String def, String[] options
			//#if MC >= 1.17
			//$$ , boolean strictOptions
			//#endif
	)
	{
		super(
				//#if MC >= 1.15
				acceleratorField,
				//#endif
				logName, def, options
				//#if MC >= 1.17
				//$$ , strictOptions
				//#endif
		);
	}

	@Unique
	private final ThreadLocal<Boolean> shouldLogToChatThisTime$TISCM = ThreadLocal.withInitial(() -> false);

	@Override
	public void setShouldLogToChat$TISCM(boolean shouldLogToChat)
	{
		if (shouldLogToChat)
		{
			this.shouldLogToChatThisTime$TISCM.set(true);
		}
		else
		{
			this.shouldLogToChatThisTime$TISCM.remove();
		}
	}

	@Inject(method = "sendPlayerMessage", at = @At("HEAD"), cancellable = true)
	private void pleaseBehaveAsARegularLoggerThisTime(
			ServerPlayer player,
			//#if MC >= 1.19
			//$$ Component[] messages,
			//#else
			BaseComponent[] messages,
			//#endif
			CallbackInfo ci
	)
	{
		if (this.shouldLogToChatThisTime$TISCM.get())
		{
			super.sendPlayerMessage(player, messages);
			ci.cancel();
		}
	}
}
