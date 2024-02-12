/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
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

package carpettisaddition.commands.lifetime.utils;

import carpettisaddition.commands.lifetime.LifeTimeTracker;
import carpettisaddition.commands.lifetime.interfaces.LifetimeTrackerTarget;
import carpettisaddition.translations.TranslationContext;
import carpettisaddition.utils.compat.DimensionWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class SpawningStatistic extends TranslationContext
{
	public long count = 0;
	public DimensionWrapper dimensionSample = null;
	public Vec3d spawningPosSample = null;

	public SpawningStatistic()
	{
		super(LifeTimeTracker.getInstance().getTranslator());
	}

	public boolean isValid()
	{
		return this.count > 0;
	}

	public void update(Entity entity)
	{
		this.count++;
		this.dimensionSample = DimensionWrapper.of(entity);
		this.spawningPosSample = ((LifetimeTrackerTarget)entity).getSpawningPosition();
	}
}
