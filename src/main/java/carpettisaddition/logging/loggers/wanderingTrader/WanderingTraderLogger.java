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

package carpettisaddition.logging.loggers.wanderingTrader;

import carpettisaddition.logging.TISAdditionLoggerRegistry;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.mixins.logger.wanderingTrader.ServerLevelAccessor;
import carpettisaddition.mixins.logger.wanderingTrader.WanderingTraderSpawnerAccessor;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.StringUtils;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.npc.WanderingTraderSpawner;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class WanderingTraderLogger extends AbstractHUDLogger
{
	public static final String NAME = "wanderingTrader";
	private static final WanderingTraderLogger INSTANCE = new WanderingTraderLogger();
	private static final BaseComponent WANDERING_TRADER_NAME = Messenger.entityType(EntityType.WANDERING_TRADER);

	private WanderingTraderLogger()
	{
		super(NAME, false);
	}

	public static WanderingTraderLogger getInstance()
	{
		return INSTANCE;
	}

	private BaseComponent pack(BaseComponent message)
	{
		String command = String.format("/log %s", this.getName());
		return Messenger.c(
				Messenger.fancy(
						Messenger.c("g [", Messenger.s("WTL"), "g ] "),
						Messenger.c(tr("header_hover"), "w \n", Messenger.s(command)),
						Messenger.ClickEvents.suggestCommand(command)
				),
				message
		);
	}

	@Override
	public String getDefaultLoggingOption()
	{
		return LoggingType.SPAWN.getName();
	}

	@Override
	public String[] getSuggestedLoggingOption()
	{
		return LoggingType.LOGGING_SUGGESTIONS;
	}

	@Override
	public BaseComponent[] onHudUpdate(String option, Player playerEntity)
	{
		if (!LoggingType.TIME.isContainedIn(option))
		{
			return null;
		}

		Level playerWorld = EntityUtils.getEntityWorld(playerEntity);
		if (!(playerWorld instanceof ServerLevel))
		{
			return null;
		}

		//#if MC >= 1.16
		//$$ WanderingTraderSpawner spawner = (WanderingTraderSpawner)((ServerLevelAccessor)playerWorld).getCustomSpawners$TISCM().stream().
		//$$ 		filter(customSpawner -> customSpawner instanceof WanderingTraderSpawner).
		//$$ 		findFirst().
		//$$ 		orElse(null);
		//#else
		WanderingTraderSpawner spawner = ((ServerLevelAccessor)playerWorld).getWanderingTraderSpawner$TISCM();
		//#endif
		if (spawner == null)
		{
			return null;
		}
		WanderingTraderSpawnerAccessor accessor = (WanderingTraderSpawnerAccessor)spawner;

		final int tickDelay = accessor.getTickDelay$TISCM();
		final int spawnDelay = accessor.getSpawnDelay$TISCM();
		final int spawnChance = accessor.getSpawnChance$TISCM();

		final int TICK_CYCLE = 24000;
		int ticksUntilSpawn = spawnDelay - (1200 - tickDelay);
		double finalSpawnChancePercent = spawnChance / 10.0;
		String percentText = StringUtils.fractionDigit(finalSpawnChancePercent, 1) + "%";

		return new BaseComponent[]{Messenger.formatting(
				Messenger.c(
						Messenger.s("WTL "),
						Messenger.s(ticksUntilSpawn),
						Messenger.s("/", ChatFormatting.DARK_GRAY),
						Messenger.s(TICK_CYCLE),
						Messenger.s(" "),
						Messenger.s(percentText, finalSpawnChancePercent >= 7.5 - 1e-4 ? ChatFormatting.DARK_GREEN : (finalSpawnChancePercent >= 5 - 1e-4 ? ChatFormatting.GOLD : ChatFormatting.DARK_RED))
				),
				ChatFormatting.GRAY
		)};
	}

	public void onWanderingTraderSpawnSuccess(Player spawnerPlayer, WanderingTrader wanderingTraderEntity)
	{
		if (!TISAdditionLoggerRegistry.__wanderingTrader)
		{
			return;
		}
		this.logToChat(option -> {
			if (!LoggingType.SPAWN.isContainedIn(option))
			{
				return null;
			}
			return new BaseComponent[]{
					pack(tr(
							"summon",
							Messenger.entity("b", spawnerPlayer),
							WANDERING_TRADER_NAME,
							Messenger.coord(wanderingTraderEntity.position(), DimensionWrapper.of(wanderingTraderEntity))
					))
			};
		});
	}

	public void onWanderingTraderSpawnFail(Player spawnerPlayer, @Nullable BlockPos spawnPos)
	{
		if (!TISAdditionLoggerRegistry.__wanderingTrader)
		{
			return;
		}
		this.logToChat(option -> {
			if (!LoggingType.FAIL.isContainedIn(option))
			{
				return null;
			}
			BaseComponent reason = spawnPos == null
					? tr("no_spawnable_pos_nearby")
					: tr("bad_spawn_pos", Messenger.coord(spawnPos, DimensionWrapper.of(spawnerPlayer)), WANDERING_TRADER_NAME);
			return new BaseComponent[]{
					pack(tr(
							"summon_failed",
							Messenger.entity("b", spawnerPlayer),
							WANDERING_TRADER_NAME,
							reason
					))
			};
		});
	}

	private enum LoggingType
	{
		SPAWN,
		FAIL,
		TIME,
		ALL;

		public static final	String[] LOGGING_SUGGESTIONS;

		public String getName()
		{
			return this.name().toLowerCase();
		}

		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		public boolean isContainedIn(String option)
		{
			return ALL.getName().equals(option) || Arrays.asList(option.split(MULTI_OPTION_SEP_REG)).contains(this.getName());
		}

		static
		{
			LOGGING_SUGGESTIONS = Arrays.stream(values()).map(LoggingType::getName).toArray(String[]::new);
		}
	}
}
