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

package carpettisaddition.mixins.rule.repeaterHalfDelay;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Mixin(DiodeBlock.class)
public abstract class AbstractRedstoneGateBlockMixin
{
	@ModifyExpressionValue(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/DiodeBlock;getDelay(Lnet/minecraft/world/level/block/state/BlockState;)I"
			)
	)
	private int modifyRepeaterDelay_tileTickEvent(int delay, BlockState state, @Coerce Level world, BlockPos pos, @Coerce Object random)
	{
		return this.modifyRepeaterDelay_getModifiedDelay(delay, (DiodeBlock)(Object)this, world, pos, state);
	}

	@ModifyExpressionValue(
			method = "checkTickOnNeighbor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/DiodeBlock;getDelay(Lnet/minecraft/world/level/block/state/BlockState;)I"
			)
	)
	private int modifyRepeaterDelay_updatePowered(int delay, Level world, BlockPos pos, BlockState state)
	{
		return this.modifyRepeaterDelay_getModifiedDelay(delay, (DiodeBlock)(Object)this, world, pos, state);
	}

	@Unique
	private int modifyRepeaterDelay_getModifiedDelay(int delay, DiodeBlock abstractRedstoneGateBlock, Level world, BlockPos pos, BlockState state)
	{
		if (CarpetTISAdditionSettings.repeaterHalfDelay)
		{
			if (abstractRedstoneGateBlock instanceof RepeaterBlock && world.getBlockState(pos.below()).getBlock() == Blocks.REDSTONE_ORE)
			{
				delay = Math.max(1, delay / 2);
			}
		}
		return delay;
	}
}
