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
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=26.3"))
@Mixin(TreeGrower.class)
public abstract class SaplingGeneratorMixin
{
	@Shadow @Final private WeightedList<ResourceKey<Feature>> flowerTrees;

	@WrapOperation(
			method = "getConfiguredFeature",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/util/random/WeightedList;getRandom(Lnet/minecraft/util/RandomSource;)Ljava/util/Optional;"
			),
			require = 2
	)
	private Optional<ResourceKey<Feature>> oakBalloonPercent_pickTree(
			WeightedList<ResourceKey<Feature>> trees,
			RandomSource random,
			Operation<Optional<ResourceKey<Feature>>> original
	)
	{
		int percent = CarpetTISAdditionSettings.oakBalloonPercent;
		if (percent >= 0 && (Object)this == TreeGrower.OAK)
		{
			boolean fancy = random.nextFloat() < percent / 100.0f;
			ResourceKey<Feature> tree;
			if (trees == this.flowerTrees)
			{
				tree = fancy ? TreeFeatures.FANCY_OAK_BEES_005 : TreeFeatures.OAK_BEES_005;
			}
			else
			{
				tree = fancy ? TreeFeatures.FANCY_OAK : TreeFeatures.OAK;
			}
			trees = WeightedList.of(tree);
		}
		return original.call(trees, random);
	}
}
