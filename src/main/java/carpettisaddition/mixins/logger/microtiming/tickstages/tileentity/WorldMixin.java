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

package carpettisaddition.mixins.logger.microtiming.tickstages.tileentity;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.TickStage;
import carpettisaddition.logging.loggers.microtiming.interfaces.IWorldTileEntity;
import carpettisaddition.logging.loggers.microtiming.tickphase.substages.TileEntitySubStage;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(Level.class)
public abstract class WorldMixin implements IWorldTileEntity
{
	private int tileEntityOrderCounter;

	@Override
	public int getTileEntityOrderCounter()
	{
		return this.tileEntityOrderCounter;
	}

	@Override
	public void setTileEntityOrderCounter(int tileEntityOrderCounter)
	{
		this.tileEntityOrderCounter = tileEntityOrderCounter;
	}

	@Inject(method = "tickBlockEntities", at = @At("HEAD"))
	private void enterStageTileEntity(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((Level)(Object)this, TickStage.TILE_ENTITY);
		this.tileEntityOrderCounter = 0;
	}

	@Inject(method = "tickBlockEntities", at = @At("TAIL"))
	private void exitStageTileEntity(CallbackInfo ci)
	{
		MicroTimingLoggerManager.setTickStage((Level)(Object)this, TickStage.UNKNOWN);
	}

	//#if MC < 11700
	@Inject(
			method = "tickBlockEntities",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/entity/TickableBlockEntity;tick()V"
			),
			locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void onTickTileEntity(CallbackInfo ci, ProfilerFiller profiler, Iterator<?> iterator, BlockEntity blockEntity)
	{
		MicroTimingLoggerManager.setSubTickStage((Level)(Object)this, new TileEntitySubStage(blockEntity, this.tileEntityOrderCounter++));  // TISCM Micro Tick logger
	}
	//#endif
}
