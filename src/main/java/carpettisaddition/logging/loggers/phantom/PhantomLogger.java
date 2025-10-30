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

package carpettisaddition.logging.loggers.phantom;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.CounterUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.StringUtils;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PhantomLogger extends AbstractLogger
{
	public static final String NAME = "phantom";
	private static final PhantomLogger INSTANCE = new PhantomLogger();
	private static final int PHANTOM_SPAWNING_TIME = 72000;
	private static final List<Integer> REMINDER_TICKS = Lists.newArrayList(PHANTOM_SPAWNING_TIME * 3 / 4, PHANTOM_SPAWNING_TIME);
	private static final BaseComponent PHANTOM_NAME = Messenger.entityType(EntityType.PHANTOM);

	private PhantomLogger()
	{
		super(NAME, false);
	}

	public static PhantomLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public @Nullable String getDefaultLoggingOption()
	{
		return LoggingOption.DEFAULT.getName();
	}

	@Override
	public @Nullable String[] getSuggestedLoggingOption()
	{
		return LoggingOption.getSuggestions();
	}

	private BaseComponent pack(BaseComponent message)
	{
		String command = String.format("/log %s", this.getName());
		return Messenger.c(
				Messenger.fancy(
						Messenger.c("g [", tr("header"), "g ] "),
						Messenger.s(command),
						Messenger.ClickEvents.suggestCommand(command)
				),
				message
		);
	}

	public void onPhantomSpawn(Player spawnerPlayer, int phantomAmount)
	{
		if (!TISAdditionLoggerRegistry.__phantom)
		{
			return;
		}
		this.log(option -> {
			if (LoggingOption.SPAWNING.isContainedIn(option))
			{
				return new BaseComponent[]{
						pack(tr("summon", Messenger.entity("b", spawnerPlayer), phantomAmount, PHANTOM_NAME))
				};
			}
			return null;
		});
	}

	public void tick()
	{
		if (!TISAdditionLoggerRegistry.__phantom)
		{
			return;
		}

		this.log((option, player) -> {
			if (LoggingOption.REMINDER.isContainedIn(option))
			{
				ServerStatsCounter serverStatHandler = ((ServerPlayer)player).getStats();
				int timeSinceRest = Mth.clamp(serverStatHandler.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
				if (REMINDER_TICKS.contains(timeSinceRest))
				{
					int timeUntilSpawn = PHANTOM_SPAWNING_TIME - timeSinceRest;
					String sinceRest = StringUtils.fractionDigit(CounterUtils.tickToMinute(timeSinceRest), 0);
					String untilSpawn = StringUtils.fractionDigit(CounterUtils.tickToMinute(timeUntilSpawn), 0);
					return new BaseComponent[]{
							pack(tr("reminder.time_since_rest", sinceRest)),
							pack(tr(timeUntilSpawn != 0 ? "reminder.regular" : "reminder.now", PHANTOM_NAME, untilSpawn))
					};
				}
			}
			return null;
		});
	}

	public enum LoggingOption
	{
		SPAWNING,
		REMINDER;

		public static final LoggingOption DEFAULT = SPAWNING;

		public String getName()
		{
			return this.name().toLowerCase();
		}

		public static String[] getSuggestions()
		{
			List<String> suggestions = Lists.newArrayList();
			suggestions.addAll(Arrays.stream(values()).map(LoggingOption::getName).collect(Collectors.toList()));
			suggestions.add(createCompoundOption(SPAWNING.getName(), REMINDER.getName()));
			return suggestions.toArray(new String[0]);
		}

		public boolean isContainedIn(String option)
		{
			return Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(this.getName());
		}
	}
}
