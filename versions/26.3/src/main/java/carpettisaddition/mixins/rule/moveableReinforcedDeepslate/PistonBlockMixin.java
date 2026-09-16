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

package carpettisaddition.mixins.rule.moveableReinforcedDeepslate;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * mc1.14 ~ mc26.2: subproject 1.15.2 (main project)
 * mc26.3+        : subproject 26.3        <--------
 */
@Mixin(PistonBaseBlock.class)
public abstract class PistonBlockMixin
{
	@ModifyExpressionValue(
			method = "isPushable",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"
			)
	)
	private static PushReaction moveableReinforcedDeepslate_modifyPushReaction(PushReaction reaction, @Local(argsOnly = true) BlockState state)
	{
		if (CarpetTISAdditionSettings.moveableReinforcedDeepslate)
		{
			if (state.is(Blocks.REINFORCED_DEEPSLATE) && reaction == PushReaction.IMMOVEABLE)
			{
				return PushReaction.PUSH_PULL;
			}
		}
		return reaction;
	}
}
