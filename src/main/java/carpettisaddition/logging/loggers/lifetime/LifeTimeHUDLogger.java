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

import carpet.logging.HUDLogger;
import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.LifeTimeWorldTracker;
import carpettisaddition.commands.lifetime.trackeddata.BasicTrackedData;
import carpettisaddition.commands.lifetime.utils.LifeTimeTrackerUtil;
import carpettisaddition.logging.loggers.AbstractHUDLogger;
import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.Messenger;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.BaseText;
import net.minecraft.util.Formatting;

import java.util.Optional;

/**
 * Independent of lifetime tracker
 * It only reads some data from the tracker
 */
public class LifeTimeHUDLogger extends AbstractHUDLogger
{
	public static final String NAME = "lifetime";

	private static final LifeTimeHUDLogger INSTANCE = new LifeTimeHUDLogger();

	public LifeTimeHUDLogger()
	{
		// strictOption value is not used here, logics are handled in LifeTimeStandardCarpetHUDLogger
		super(NAME, false);
	}

	public static LifeTimeHUDLogger getInstance()
	{
		return INSTANCE;
	}

	@Override
	public HUDLogger createCarpetLogger()
	{
		return new LifeTimeStandardCarpetHUDLogger();
	}

	@Override
	public BaseText[] onHudUpdate(String option, PlayerEntity playerEntity)
	{
		LifeTimeWorldTracker tracker = LifeTimeTracker.getInstance().getTracker(EntityUtils.getEntityWorld(playerEntity));
		if (tracker != null)
		{
			Optional<EntityType<?>> entityTypeOptional = LifeTimeTrackerUtil.getEntityTypeFromName(option);
			if (entityTypeOptional.isPresent())
			{
				EntityType<?> entityType = entityTypeOptional.get();
				BasicTrackedData data = tracker.getDataMap().getOrDefault(entityType, new BasicTrackedData());
				return new BaseText[]{Messenger.c(
						Messenger.formatting(Messenger.copy(Messenger.entityType(entityType)), Formatting.GRAY),
						"g : ",
						"e " + data.getSpawningCount(),
						"g /",
						"r " + data.getRemovalCount(),
						"w  ",
						data.lifeTimeStatistic.getCompressedResult(false)
				)};
			}
		}
		return null;
	}
}
