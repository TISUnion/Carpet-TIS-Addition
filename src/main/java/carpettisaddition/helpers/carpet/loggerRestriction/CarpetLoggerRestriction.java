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

package carpettisaddition.helpers.carpet.loggerRestriction;

import carpet.logging.Logger;
import carpettisaddition.CarpetTISAdditionMod;
import carpettisaddition.translations.Translator;
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

import java.util.function.Supplier;

public class CarpetLoggerRestriction
{
	private static final Translator translator = new Translator("misc.logger_rule_switch");

	public static RestrictionCheckResult checkLoggerSubscribable(Logger logger, PlayerEntity player, String option)
	{
		return ((RestrictiveLogger)logger).canPlayerSubscribe(player, option);
	}

	public static boolean isLoggerSubscribable(Logger logger, PlayerEntity player, String option)
	{
		return checkLoggerSubscribable(logger, player, option).isPassed();
	}

	public static void addLoggerRuleSwitch(Logger logger, String ruleName, Supplier<String> ruleValueProvider)
	{
		((RestrictiveLogger)logger).addSubscriptionRestriction((player, option) -> {
			BaseText message = Messenger.hover(
					translator.tr("permission_denied", logger.getLogName()),
					translator.tr("rule_hint", ruleName)
			);
			if (!(player instanceof ServerPlayerEntity))
			{
				CarpetTISAdditionMod.LOGGER.warn("subscriptionChecker receives a player {} that is not a ServerPlayerEntity", player);
				return RestrictionCheckResult.ok();
			}

			ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;  // for mc1.21.2+, where getCommandSource requires being on the server-side
			if (CarpetModUtil.canUseCarpetCommand(serverPlayer.getCommandSource()))
			{
				Messenger.click(
						message,
						Messenger.ClickEvents.suggestCommand("/carpet " + ruleName)
				);
			}
			return RestrictionCheckResult.bool(
					CarpetModUtil.canUseCommand(serverPlayer.getCommandSource(), ruleValueProvider.get()),
					message
			);
		});
	}
}
