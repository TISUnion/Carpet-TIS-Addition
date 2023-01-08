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

package carpettisaddition.mixins.rule.creativeOpenContainerForcibly;

import carpettisaddition.helpers.rule.creativeOpenContainerForcibly.CreativeOpenContainerForciblyHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderChestBlock.class)
public abstract class EnderChestBlockMixin
{
	@Redirect(
			//#if MC >= 11500
			method = "onUse",
			//#else
			//$$ method = "activate",
			//#endif
			at = @At(
					value = "INVOKE",
					//#if MC >= 11600
					//$$ target = "Lnet/minecraft/block/BlockState;isSolidBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
					//#else
					target = "Lnet/minecraft/block/BlockState;isSimpleFullBlock(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z"
					//#endif
			),
			require = 0
	)
	private boolean isSimpleFullBlockAndNotCreative(BlockState blockState, BlockView view, BlockPos pos, /* parent method parameters -> */ BlockState state, World world, BlockPos pos2, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if (CreativeOpenContainerForciblyHelper.canOpenForcibly(player))
		{
			return false;
		}

		// vanilla
		//#if MC >= 11600
		//$$ return blockState.isSolidBlock(view, pos);
		//#else
		return blockState.isSimpleFullBlock(view, pos);
		//#endif
	}
}
