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

package carpettisaddition.mixins.rule.oakBalloonPercent;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.class_8813;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">1.20.2"))
@Mixin(class_8813.class)
public abstract class TreeFeaturesMixin
{
	@ModifyExpressionValue(
			method = "method_54087",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/class_8813;field_46522:F"
			)
	)
	private float oakBalloonPercent_modifyChance(float chance)
	{
		if (CarpetTISAdditionSettings.oakBalloonPercent > 0)
		{
			class_8813 self = (class_8813)(Object)this;
			if (self == class_8813.OAK)
			{
				chance = CarpetTISAdditionSettings.oakBalloonPercent / 100.0f;
			}
		}
		return chance;
	}
}
