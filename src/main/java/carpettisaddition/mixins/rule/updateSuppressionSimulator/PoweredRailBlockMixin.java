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

package carpettisaddition.mixins.rule.updateSuppressionSimulator;

import carpettisaddition.helpers.rule.updateSuppressionSimulator.UpdateSuppressionSimulator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PoweredRailBlock.class)
public abstract class PoweredRailBlockMixin
{
	@ModifyVariable(
			method = "updateState",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
					ordinal = 0
			),
			ordinal = 1
	)
	private boolean updateSuppressionSimulatorImpl(boolean newPoweredState, BlockState state, Level world, BlockPos pos, Block neighbor)
	{
		if (UpdateSuppressionSimulator.isActivated())
		{
			if (!newPoweredState && world.getBlockState(pos.below()).getBlock() == Blocks.LAPIS_ORE)
			{
				UpdateSuppressionSimulator.kaboom();
			}
		}
		return newPoweredState;
	}
}
