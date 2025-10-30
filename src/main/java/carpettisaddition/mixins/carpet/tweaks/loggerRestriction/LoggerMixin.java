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
import carpettisaddition.helpers.carpet.loggerRestriction.RestrictionCheckResult;
import carpettisaddition.helpers.carpet.loggerRestriction.RestrictiveLogger;
import carpettisaddition.helpers.carpet.loggerRestriction.SubscriptionChecker;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(Logger.class)
public abstract class LoggerMixin implements RestrictiveLogger
{
	private final List<SubscriptionChecker> subscriptionCheckers = Lists.newArrayList();

	public void addSubscriptionRestriction(SubscriptionChecker subscriptionChecker)
	{
		this.subscriptionCheckers.add(subscriptionChecker);
	}

	@Override
	public RestrictionCheckResult canPlayerSubscribe(Player player, @Nullable String option)
	{
		for (SubscriptionChecker checker : this.subscriptionCheckers)
		{
			RestrictionCheckResult result = checker.check(player, option);
			if (!result.isPassed())
			{
				return result;
			}
		}
		return RestrictionCheckResult.ok();
	}
}
