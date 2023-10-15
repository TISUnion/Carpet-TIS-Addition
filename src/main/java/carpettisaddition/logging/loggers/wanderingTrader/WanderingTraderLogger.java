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
import carpettisaddition.logging.loggers.AbstractLogger;
import carpettisaddition.utils.Messenger;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.text.ClickEvent;

public class WanderingTraderLogger extends AbstractLogger
{
	public static final String NAME = "wanderingTrader";
	private static final WanderingTraderLogger INSTANCE = new WanderingTraderLogger();
	private static final BaseText WANDERING_TRADER_NAME = Messenger.entityType(EntityType.WANDERING_TRADER);

	private WanderingTraderLogger()
	{
		super(NAME, false);
	}

	public static WanderingTraderLogger getInstance()
	{
		return INSTANCE;
	}

	private BaseText pack(BaseText message)
	{
		String command = String.format("/log %s", this.getName());
		return Messenger.c(
				Messenger.fancy(
						Messenger.c("g [", Messenger.s("WTL"), "g ] "),
						Messenger.c(tr("header_hover"), "w \n", Messenger.s(command)),
						new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
				),
				message
		);
	}

	public void onWanderingTraderSpawn(PlayerEntity spawnerPlayer, WanderingTraderEntity wanderingTraderEntity)
	{
		if (!TISAdditionLoggerRegistry.__wanderingTrader)
		{
			return;
		}
		this.log(option -> {
			return new BaseText[]{
					pack(tr(
							"summon",
							Messenger.entity("b", spawnerPlayer),
							WANDERING_TRADER_NAME,
							Messenger.coord(wanderingTraderEntity.getPos(), DimensionWrapper.of(wanderingTraderEntity))
					))
			};
		});
	}
}
