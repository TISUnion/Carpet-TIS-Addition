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

package carpettisaddition.commands.xcounter;

import carpettisaddition.utils.EntityUtils;
import carpettisaddition.utils.HopperCounterUtils;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class XpCounterHopperTicker
{
	public static void tickHopper(HopperBlockEntity hopper)
	{
		DyeColor color = HopperCounterUtils.getWoolColorForHopper(hopper);
		if (color == null)
		{
			return;
		}

		for (ExperienceOrb xpOrb : getInputXpOrbEntities(hopper))
		{
			XpCounterCommand.getInstance().record(
					color,
					xpOrb.getValue(),
					EntityUtils.getXpOrbPickingCount(xpOrb)
			);
			//#if MC >= 11700
			//$$ xpOrb.discard();
			//#else
			xpOrb.remove();
			//#endif
		}
	}

	/**
	 * Reference: {@link net.minecraft.world.level.block.entity.HopperBlockEntity#getInputItemEntities}
	 */
	private static List<ExperienceOrb> getInputXpOrbEntities(HopperBlockEntity hopper)
	{
		Level world = hopper.getLevel();
		if (world == null)
		{
			return Collections.emptyList();
		}


		//#if MC >= 12006
		//$$ var box = hopper.getInputAreaShape().offset(hopper.getHopperX() - 0.5, hopper.getHopperY() - 0.5, hopper.getHopperZ() - 0.5);
		//$$ return world.getEntitiesByClass(ExperienceOrb.class, box, EntityPredicates.VALID_ENTITY);
		//#else
		return hopper.getSuckShape()
				.toAabbs()
				.stream()
				.flatMap(box ->
						world.getEntitiesOfClass(
								ExperienceOrb.class,
								box.move(hopper.getLevelX() - 0.5, hopper.getLevelY() - 0.5, hopper.getLevelZ() - 0.5),
								EntitySelector.ENTITY_STILL_ALIVE
						).stream()
				)
				.collect(Collectors.toList());
		//#endif
	}
}
