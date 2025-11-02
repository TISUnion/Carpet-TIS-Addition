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

package carpettisaddition.mixins.rule.fortressNetherBricksPackSpawningFix;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Objects;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.18.2 <1.21.5"))
@Mixin(NaturalSpawner.class)
public abstract class SpawnHelperMixin
{
	@WrapOperation(
			method = "canSpawnMobAt",
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;contains(Ljava/lang/Object;)Z"
			)
	)
	private static boolean fortressNetherBricksPackSpawningFix_dontJustCallContains(List<MobSpawnSettings.SpawnerData> entries, Object value, Operation<Boolean> original)
	{
		if (CarpetTISAdditionSettings.fortressNetherBricksPackSpawningFix)
		{
			if (value instanceof MobSpawnSettings.SpawnerData toTestEntry)
			{
				for (MobSpawnSettings.SpawnerData entry : entries)
				{
					if (spawnEntryEquals(entry, toTestEntry))
					{
						return true;
					}
				}
			}
			return false;
		}

		// vanilla
		return original.call(entries, value);
	}

	@Unique
	private static boolean spawnEntryEquals(MobSpawnSettings.SpawnerData a, MobSpawnSettings.SpawnerData b)
	{
		if (a == b) return true;
		if (a == null || b == null) return false;

		//#if MC >= 12105
		//$$ // XXX: check if vanilla has already fixed this issue, since now it's a record class
		//$$ return Objects.equals(a.type(), b.type()) && a.minCount() == b.minCount() && a.maxCount() == b.maxCount();
		//#else
		return Objects.equals(a.type, b.type) && a.minCount == b.minCount && a.maxCount == b.maxCount;
		//#endif
	}
}
