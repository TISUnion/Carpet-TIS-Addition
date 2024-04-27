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

package carpettisaddition.mixins.rule.mobcapsDisplayIgnoreMisc;

import carpet.utils.SpawnReporter;
import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.SpawnGroup;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Mixin(SpawnReporter.class)
public abstract class SpawnReporterMixin
{
	@ModifyExpressionValue(
			method = "printMobcapsForDimension",
			at = @At(
					value = "INVOKE",
					//#if MC >= 12002
					//$$ target = "Lcarpet/utils/SpawnReporter;cachedMobCategories()[Lnet/minecraft/entity/SpawnGroup;"
					//#elseif MC >= 11600
					target = "Lnet/minecraft/entity/SpawnGroup;values()[Lnet/minecraft/entity/SpawnGroup;"
					//#else
					//$$ target = "Lnet/minecraft/entity/EntityCategory;values()[Lnet/minecraft/entity/EntityCategory;"
					//#endif
			)
	)
	private static SpawnGroup[] mobcapsDisplayIgnoreMisc(SpawnGroup[] values)
	{
		if (CarpetTISAdditionSettings.mobcapsDisplayIgnoreMisc)
		{
			values = Arrays.stream(values).
					filter(entityCategory -> entityCategory != SpawnGroup.MISC).
					toArray(SpawnGroup[]::new);
		}
		return values;
	}
}
