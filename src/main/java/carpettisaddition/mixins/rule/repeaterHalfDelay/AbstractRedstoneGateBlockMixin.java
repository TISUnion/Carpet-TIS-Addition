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
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;

@Mixin(AbstractRedstoneGateBlock.class)
public abstract class AbstractRedstoneGateBlockMixin
{
	@ModifyExpressionValue(
			//#if MC >= 11500
			method = "scheduledTick",
			//#else
			//$$ method = "onScheduledTick",
			//#endif
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/AbstractRedstoneGateBlock;getUpdateDelayInternal(Lnet/minecraft/block/BlockState;)I"
			)
	)
	private int modifyRepeaterDelay_tileTickEvent(int delay, BlockState state, @Coerce World world, BlockPos pos, @Coerce Object random)
	{
		return this.getModifiedDelay(delay, (AbstractRedstoneGateBlock)(Object)this, world, pos, state);
	}

	@ModifyExpressionValue(
			method = "updatePowered",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/block/AbstractRedstoneGateBlock;getUpdateDelayInternal(Lnet/minecraft/block/BlockState;)I"
			)
	)
	private int modifyRepeaterDelay_updatePowered(int delay, World world, BlockPos pos, BlockState state)
	{
		return this.getModifiedDelay(delay, (AbstractRedstoneGateBlock)(Object)this, world, pos, state);
	}

	private int getModifiedDelay(int delay, AbstractRedstoneGateBlock abstractRedstoneGateBlock, World world, BlockPos pos, BlockState state)
	{
		if (CarpetTISAdditionSettings.repeaterHalfDelay)
		{
			if (abstractRedstoneGateBlock instanceof RepeaterBlock && world.getBlockState(pos.down()).getBlock() == Blocks.REDSTONE_ORE)
			{
				delay = Math.max(1, delay / 2);
			}
		}
		return delay;
	}
}
