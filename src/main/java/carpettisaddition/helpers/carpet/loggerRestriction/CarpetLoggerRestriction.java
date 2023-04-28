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
import carpettisaddition.utils.CarpetModUtil;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.player.PlayerEntity;

import java.util.function.Supplier;

public class CarpetLoggerRestriction
{
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
			return RestrictionCheckResult.bool(
					CarpetModUtil.canUseCommand(player.getCommandSource(), ruleValueProvider.get()),
					Messenger.tr("TODO")
			);
		});
	}
}
