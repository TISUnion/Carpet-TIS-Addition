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

package carpettisaddition.commands.lifetime.trackeddata;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.text.BaseText;

//#if MC >= 11700
import carpettisaddition.mixins.command.lifetime.data.ExperienceOrbEntityAccessor;
//#endif

public class ExperienceOrbTrackedData extends ExtraCountTrackedData
{
	@Override
	protected long getExtraCount(Entity entity)
	{
		if (entity instanceof ExperienceOrbEntity)
		{
			return
					((ExperienceOrbEntity)entity).getExperienceAmount()
					//#if MC >= 11700
					* ((ExperienceOrbEntityAccessor)entity).getPickingCount()
					//#endif
					;
		}
		return 0L;
	}

	@Override
	protected BaseText getCountDisplayText()
	{
		return tr("experience_amount");
	}

	@Override
	protected String getCountButtonString()
	{
		return "E";
	}
}
