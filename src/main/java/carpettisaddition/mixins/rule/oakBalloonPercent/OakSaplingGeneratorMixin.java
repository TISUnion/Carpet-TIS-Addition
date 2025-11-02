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
import net.minecraft.world.level.block.grower.OakTreeGrower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//#if MC >= 11900
//$$ import net.minecraft.util.RandomSource;
//#else
import java.util.Random;
//#endif

@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = "<=1.20.2"))
@Mixin(OakTreeGrower.class)
public abstract class OakSaplingGeneratorMixin
{
	@ModifyExpressionValue(
			method = "getConfiguredFeature",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11900
					//$$ target = "Lnet/minecraft/util/RandomSource;nextInt(I)I"
					//#else
					target = "Ljava/util/Random;nextInt(I)I"
					//#endif
			)
	)
	private int oakBalloonPercent_modifyRandomResult(
			int randomResult, Random random
			//#if MC >= 11500
			, boolean bee
			//#endif
	)
	{
		if (CarpetTISAdditionSettings.oakBalloonPercent > 0)
		{
			boolean balloon = random.nextInt(100) < CarpetTISAdditionSettings.oakBalloonPercent;
			randomResult = balloon ? 0 : 1;
		}
		return randomResult;
	}
}
