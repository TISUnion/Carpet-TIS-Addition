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
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin
{
	// This changed is introduced in 1.20-pre2, let's revert it
	@ModifyExpressionValue(
			method = "getRenderConnectionType(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Z)Lnet/minecraft/block/enums/WireConnection;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;",
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
	//$$ 		method = "getStateForNeighborUpdate",
	//$$ 		at = @At(
	//$$ 				value = "INVOKE",
	//$$ 				target = "Lnet/minecraft/block/RedstoneWireBlock;canRunOnTop(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
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
