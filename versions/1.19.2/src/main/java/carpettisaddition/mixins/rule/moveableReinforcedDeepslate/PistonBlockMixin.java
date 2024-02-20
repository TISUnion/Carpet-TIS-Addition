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

package carpettisaddition.mixins.rule.moveableReinforcedDeepslate;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin
{
	@ModifyExpressionValue(
			method = "isMovable",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/block/Blocks;REINFORCED_DEEPSLATE:Lnet/minecraft/block/Block;"
			)
	)
	private static Block moveableReinforcedDeepslate_makeTheEqualCheckFailed(Block block)
	{
		if (CarpetTISAdditionSettings.moveableReinforcedDeepslate)
		{
			// there's a `state.isAir()` check before the if statement, so the block is certainly not an air
			block = Blocks.AIR;
		}
		return block;
	}
}
