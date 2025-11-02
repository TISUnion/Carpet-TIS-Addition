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
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PoweredRailBlock.class)
public abstract class PoweredRailBlockMixin
{
	@Inject(
			method = "updateState",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
					ordinal = 0
			),
			cancellable = true
	)
	private void updateSkippingSimulatorImpl(
			CallbackInfo ci,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos pos,
			@Local(ordinal = 1) boolean newPoweredState
	)
	{
		if (UpdateSkippingSimulator.isActivated())
		{
			if (!newPoweredState && world.getBlockState(pos.below()).getBlock() == Blocks.DEEPSLATE_LAPIS_ORE)
			{
				UpdateSkippingSimulator.kaboom(world, pos);
				ci.cancel();
			}
		}
	}
}
