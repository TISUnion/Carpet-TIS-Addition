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

package carpettisaddition.mixins.rule.oakBalloonPercent;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">26.3"))
@Mixin(TreeGrower.class)
public abstract class SaplingGeneratorMixin
{
	@ModifyExpressionValue(
			method = "getConfiguredFeature",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/world/level/block/grower/TreeGrower;trees:Lnet/minecraft/util/random/WeightedList;",
					opcode = Opcodes.GETFIELD
			)
	)
	private WeightedList<ResourceKey<Feature>> oakBalloonPercent_modifyWeightList(
			WeightedList<ResourceKey<Feature>> original,
			@Local(argsOnly = true) RandomSource random
	)
	{
		if (CarpetTISAdditionSettings.oakBalloonPercent > 0)
		{
			TreeGrower self = (TreeGrower)(Object)this;
			if (self == TreeGrower.OAK)
			{
				if (CarpetTISAdditionSettings.oakBalloonPercent / 100.0f > random.nextFloat())
				{
					return WeightedList.of(TreeFeatures.FANCY_OAK);
				}
				else
				{
					return WeightedList.of(TreeFeatures.OAK);
				}
			}
		}
		return original;
	}
}
