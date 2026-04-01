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

package carpettisaddition.logging.loggers.entityIdCounter;

import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.mixins.command.info.server.EntityAccessor;
import carpettisaddition.utils.Messenger;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.world.entity.player.Player;

public class EntityIdCounterHUDLogger extends AbstractHUDLogger
{
	public static final String NAME = "entityIdCounter";

	private static final EntityIdCounterHUDLogger INSTANCE = new EntityIdCounterHUDLogger();

	private EntityIdCounterHUDLogger()
	{
		super(NAME, true);
	}

	public static EntityIdCounterHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public BaseComponent[] onHudUpdate(String option, Player playerEntity)
	{
		int value = EntityAccessor.getEntityIdCounter$TISCM().get();
		double overflowTo0Percent = EntityIdCounterUtils.getPercentOfOverflowToZero(value);
		return new BaseComponent[]{
				Messenger.c(String.format("g EID %d %.2f%%", value, overflowTo0Percent))
		};
	}
}
