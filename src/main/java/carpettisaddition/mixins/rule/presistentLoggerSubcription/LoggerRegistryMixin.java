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

package carpettisaddition.mixins.rule.presistentLoggerSubcription;

import carpet.logging.LoggerRegistry;
import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.helpers.rule.persistentLoggerSubscription.LoggerSubscriptionStorage;
import carpettisaddition.utils.GameUtils;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// improves priority due to @ModifyVariable into locals
@Mixin(value = LoggerRegistry.class, priority = 500)
public abstract class LoggerRegistryMixin
{
	private static final ThreadLocal<Boolean> appliedPersistentLoggerSubscription = ThreadLocal.withInitial(() -> false);

	@Inject(
			method = "playerConnected",
			at = @At(
					value = "FIELD",
					target = "Lcarpet/logging/LoggerRegistry;loggerRegistry:Ljava/util/Map;",
					remap = false
			),
			locals = LocalCapture.CAPTURE_FAILHARD,
			remap = false
	)
	private static void tweaksLoggingOptions(PlayerEntity player, CallbackInfo ci, boolean firstTime)
	{
		if (CarpetTISAdditionSettings.persistentLoggerSubscription && firstTime)
		{
			if (LoggerSubscriptionStorage.getInstance().restoreSubscription(player))
			{
				appliedPersistentLoggerSubscription.set(true);
			}
		}
	}

	@ModifyVariable(
			method = "playerConnected",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/Map;values()Ljava/util/Collection;",
					remap = true
			),
			remap = false
	)
	private static boolean dontSetDefaultLogger(boolean value)
	{
		if (appliedPersistentLoggerSubscription.get())
		{
			value = false;
			appliedPersistentLoggerSubscription.set(false);
		}
		return value;
	}

	@Inject(method = "subscribePlayer", at = @At("HEAD"), remap = false)
	private static void onPlayerLogSomething(String playerName, String logName, String option, CallbackInfo ci)
	{
		PlayerEntity player = GameUtils.getPlayerFromName(playerName);
		if (player != null)
		{
			LoggerSubscriptionStorage.getInstance().addSubscription(player, logName, option);
		}
	}

	@Inject(method = "unsubscribePlayer", at = @At("HEAD"), remap = false)
	private static void onPlayerUnlogSomething(String playerName, String logName, CallbackInfo ci)
	{
		PlayerEntity player = GameUtils.getPlayerFromName(playerName);
		if (player != null)
		{
			LoggerSubscriptionStorage.getInstance().removeSubscription(player, logName);
		}
	}
}
