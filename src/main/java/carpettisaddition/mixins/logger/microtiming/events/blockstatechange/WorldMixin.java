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

package carpettisaddition.mixins.logger.microtiming.events.blockstatechange;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/*
 * BlockState Change starts
 */
@Mixin(Level.class)
public abstract class WorldMixin
{
	@Shadow public abstract BlockState getBlockState(BlockPos pos);

	@Inject(
			//#if MC >= 11600
			//$$ method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
			//#else
			method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
			//#endif
			at = @At("HEAD")
	)
	private void startSetBlockState(
			BlockPos pos, BlockState newState, int flags,
			//#if MC >= 11600
			//$$ int maxUpdateDepth,
			//#endif
			CallbackInfoReturnable<Boolean> cir,
			@Share("previousBlockState") LocalRef<BlockState> previousBlockState
			)
	{
		if (MicroTimingLoggerManager.isLoggerActivated())
		{
			BlockState oldState = this.getBlockState(pos);
			previousBlockState.set(oldState);
			MicroTimingLoggerManager.onSetBlockState((Level)(Object)this, pos, oldState, newState, null, flags, EventType.ACTION_START);
		}
	}

	@Inject(
			//#if MC >= 11600
			//$$ method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
			//#else
			method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
			//#endif
			at = @At("RETURN")
	)
	private void endSetBlockState(
			BlockPos pos, BlockState newState, int flags,
			//#if MC >= 11600
			//$$ int maxUpdateDepth,
			//#endif
			CallbackInfoReturnable<Boolean> cir,
			@Share("previousBlockState") LocalRef<BlockState> previousBlockState
	)
	{
		if (MicroTimingLoggerManager.isLoggerActivated() && previousBlockState.get() != null)
		{
			BlockState oldState = previousBlockState.get();
			MicroTimingLoggerManager.onSetBlockState((Level)(Object)this, pos, oldState, newState, cir.getReturnValue(), flags, EventType.ACTION_END);
		}
	}
}
