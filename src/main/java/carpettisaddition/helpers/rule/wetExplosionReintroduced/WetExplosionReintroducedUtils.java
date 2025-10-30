/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.helpers.rule.wetExplosionReintroduced;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.mixins.rule.wetExplosionReintroduced.ExplosionAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Explosion;

public class WetExplosionReintroducedUtils
{
	public static boolean ruleEnabled()
	{
		return CarpetTISAdditionSettings.wetExplosionReintroduced;
	}

	public static boolean isWetExplosion(Explosion explosion)
	{
		if (!(explosion instanceof ExplosionAccessor))
		{
			return false;
		}
		Entity explodingEntity = ((ExplosionAccessor)explosion).getEntity();
		return explodingEntity != null && explodingEntity.isInWater();
	}

	public static boolean isBenefitedEntity(Entity entity)
	{
		return entity instanceof ItemEntity || entity instanceof ArmorStand || entity instanceof HangingEntity;
	}

	public static boolean check(Explosion explosion, Entity entity)
	{
		//#if 12102 <= MC && MC < 12106
		//$$ // "Wet Explosion" is a vanilla feature in [1.21.2, 1.21.6)
		//$$ return false;
		//#else
		return ruleEnabled() && isWetExplosion(explosion) && isBenefitedEntity(entity);
		//#endif
	}
}
