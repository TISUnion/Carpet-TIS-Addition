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

package carpettisaddition.logging.loggers.lifetime;

//#if MC >= 11500
import carpet.logging.HUDLogger;
//#else
//$$ import carpettisaddition.logging.compat.ExtensionHUDLogger;
//#endif

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.logging.TISAdditionLoggerRegistry;
import net.minecraft.entity.player.PlayerEntity;

public class LifeTimeStandardCarpetHUDLogger extends
		//#if MC >= 11500
		HUDLogger
		//#else
		//$$ ExtensionHUDLogger
		//#endif
{
	public LifeTimeStandardCarpetHUDLogger()
	{
		super(
				TISAdditionLoggerRegistry.getLoggerField(LifeTimeHUDLogger.NAME), LifeTimeHUDLogger.NAME, null, null
				//#if MC >= 11700
				, false
				//#endif
		);
	}

	@Override
	public void addPlayer(String playerName, String option)
	{
		super.addPlayer(playerName, option);
		PlayerEntity player = this.playerFromName(playerName);
		if (player != null)
		{
			if (!LifeTimeTrackerUtil.getEntityTypeFromName(option).isPresent())
			{
				LifeTimeTracker.getInstance().sendUnknownEntity(player.getCommandSource(), option);
			}
		}
	}

	@Override
	public String[] getOptions()
	{
		return LifeTimeTracker.getInstance().getAvailableEntityType().toArray(String[]::new);
	}
}
