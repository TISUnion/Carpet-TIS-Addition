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

package carpettisaddition.mixins.logger.microtiming.events.tiletick;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Tile Tick
 * for MC 1.18+, executing tile tick stuffs are mixined in TileTickListMixin
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Inject(
			method = "tickBlock",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lnet/minecraft/block/BlockState;scheduledTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V"
					//#else
					//$$ target = "Lnet/minecraft/block/BlockState;scheduledTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V"
					//#endif
			)
	)
	private void preExecuteBlockTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_START);
	}

	@Inject(
			method = "tickBlock",
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Lnet/minecraft/block/BlockState;scheduledTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V",
					//#else
					//$$ target = "Lnet/minecraft/block/BlockState;scheduledTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)V",
					//#endif
					shift = At.Shift.AFTER
			)
	)
	private void postExecuteBlockTileTickEvent(ScheduledTick<Block> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_END);
	}

	@Inject(
			method = "tickFluid",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/fluid/FluidState;onScheduledTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"
			)
	)
	private void preExecuteFluidTileTickEvent(ScheduledTick<Fluid> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_START);
	}

	@Inject(
			method = "tickFluid",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/fluid/FluidState;onScheduledTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V",
					shift = At.Shift.AFTER
			)
	)
	private void postExecuteFluidTileTickEvent(ScheduledTick<Fluid> event, CallbackInfo ci)
	{
		MicroTimingLoggerManager.onExecuteTileTickEvent((ServerWorld)(Object)this, event, EventType.ACTION_END);
	}
}
