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

package carpettisaddition.mixins.rule.dustTrapdoorReintroduced;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedStoneWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedStoneWireBlock.class)
public abstract class RedstoneWireBlockMixin
{
	// This changed is introduced in 1.20-pre2, let's revert it
	@ModifyExpressionValue(
			method = "getConnectingSide(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Z)Lnet/minecraft/world/level/block/state/properties/RedstoneSide;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;",
					ordinal = 0
			)
	)
	private Block dustTrapdoorReintroduced_makeOpenedTrapdoorNotAbleToRedirectDustAgain(Block block)
	{
		if (CarpetTISAdditionSettings.dustTrapdoorReintroduced)
		{
			// Blocks.AIR instanceof TrapdoorBlock == false
			block = Blocks.AIR;
		}
		return block;
	}

	//#if MC >= 12002
	//$$ // This changed is introduced in 23w35a (1.20.2 snapshot), let's revert it
	//$$ @ModifyExpressionValue(
	//$$ 		method = "updateShape",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/world/level/block/RedStoneWireBlock;canSurviveOn(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
	//$$ 		),
	//$$ 		allow = 1
	//$$ )
	//$$ private boolean dustTrapdoorReintroduced_makeDustSurviveOnOpenedTrapdoorAgain(boolean canRunOnTop)
	//$$ {
	//$$ 	if (CarpetTISAdditionSettings.dustTrapdoorReintroduced)
	//$$ 	{
	//$$ 		canRunOnTop = true;
	//$$ 	}
	//$$ 	return canRunOnTop;
	//$$ }
	//#endif
}
