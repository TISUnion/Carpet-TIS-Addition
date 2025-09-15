/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package carpettisaddition.mixins.rule.updateSkippingSimulator;

import carpettisaddition.helpers.rule.updateSkippingSimulator.UpdateSkippingSimulator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PoweredRailBlock.class)
public abstract class PoweredRailBlockMixin
{
	@ModifyVariable(
			method = "updateBlockState",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z",
					ordinal = 0
			),
			ordinal = 1
	)
	private boolean updateSkippingSimulatorImpl(boolean newPoweredState, BlockState state, World world, BlockPos pos, Block neighbor)
	{
		if (UpdateSkippingSimulator.isActivated())
		{
			if (!newPoweredState && world.getBlockState(pos.down()).getBlock() == Blocks.DEEPSLATE_LAPIS_ORE)
			{
				UpdateSkippingSimulator.kaboom(world, pos);
			}
		}
		return newPoweredState;
	}
}
