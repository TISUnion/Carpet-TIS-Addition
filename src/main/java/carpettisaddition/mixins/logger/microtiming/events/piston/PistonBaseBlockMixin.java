/*
 * This file is part of the Carpet TIS Addition project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package carpettisaddition.mixins.logger.microtiming.events.piston;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PistonBaseBlock.class)
public abstract class PistonBaseBlockMixin
{
	@ModifyReceiver(
			method = "checkIfExtend",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/piston/PistonStructureResolver;resolve()Z"
			)
	)
	private PistonStructureResolver recordResolver(PistonStructureResolver obj, @Share("resolver") LocalRef<PistonStructureResolver> resolver)
	{
		resolver.set(obj);
		return obj;
	}

	@ModifyExpressionValue(
			method = "checkIfExtend",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/piston/PistonStructureResolver;resolve()Z"
			)
	)
	private boolean onPistonStructureResolverResolved_checkIfExtend(
			boolean success,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos pos,
			@Share("resolver") LocalRef<PistonStructureResolver> resolver
	)
	{
		if (resolver.get() != null)
		{
			MicroTimingLoggerManager.onPistonComputePushStructureEvent(world, pos, (Block)(Object)this, success, resolver.get());
		}
		return success;
	}

	@ModifyExpressionValue(
			method = "moveBlocks",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/piston/PistonStructureResolver;resolve()Z"
			)
	)
	private boolean onPistonStructureResolverResolved_moveBlocks(
			boolean success,
			@Local(argsOnly = true) Level world,
			@Local(argsOnly = true) BlockPos pos,
			@Local PistonStructureResolver resolver
	)
	{
		MicroTimingLoggerManager.onPistonComputePushStructureEvent(world, pos, (Block)(Object)this, success, resolver);
		return success;
	}
}
