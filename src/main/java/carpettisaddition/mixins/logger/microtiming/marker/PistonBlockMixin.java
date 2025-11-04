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

package carpettisaddition.mixins.logger.microtiming.marker;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBlockMixin
{
	@SuppressWarnings("rawtypes")
	@Inject(
			method = "moveBlocks",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11500
							target = "Ljava/util/Map;remove(Ljava/lang/Object;)Ljava/lang/Object;"
							//#else
							//$$ target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11700
					//$$ target = "Lnet/minecraft/world/level/Level;setBlockEntity(Lnet/minecraft/world/level/block/entity/BlockEntity;)V",
					//#else
					target = "Lnet/minecraft/world/level/Level;setBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;)V",
					//#endif
					shift = At.Shift.AFTER,
					ordinal = 0
			)
	)
	private void moveMicroTimingMarkerAsWell(
			Level world, BlockPos pos, Direction dir, boolean retract,
			CallbackInfoReturnable<Boolean> cir,
			@Local(ordinal = 1) Direction direction,
			@Local(ordinal = 2) BlockPos blockPos4
	)
	{
		MicroTimingLoggerManager.moveMarker(world, blockPos4.relative(direction.getOpposite()), direction);
	}
}
