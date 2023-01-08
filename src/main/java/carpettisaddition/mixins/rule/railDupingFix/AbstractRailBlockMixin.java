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

package carpettisaddition.mixins.rule.railDupingFix;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(AbstractRailBlock.class)
public abstract class AbstractRailBlockMixin
{
	@Inject(
			method = "neighborUpdate",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/AbstractRailBlock;updateBlockState(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;)V"
			),
			require = 1,
			cancellable = true
	)
	private void checkIfRailStillExists(BlockState state, World world, BlockPos pos, Block block, BlockPos neighborPos, boolean moved, CallbackInfo ci)
	{
		if (CarpetTISAdditionSettings.railDupingFix)
		{
			if (!(world.getBlockState(pos).getBlock() instanceof AbstractRailBlock))
			{
				ci.cancel();
			}
		}
	}

}
