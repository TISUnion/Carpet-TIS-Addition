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

package carpettisaddition.mixins.logger.microtiming.events.blockupdate;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.BlockUpdateType;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import carpettisaddition.utils.ModIds;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 1.19+ manual stack update chain logic, for block /state update event stuffs
 */
public abstract class ChainRestrictedNeighborUpdaterMixins
{
	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.level.redstone.CollectingNeighborUpdater$SimpleNeighborUpdate")
	public static class SimpleEntryMixin
	{
		@Shadow @Final private Block block;
		@Shadow @Final private BlockPos pos;

		@Inject(method = "runNext", at = @At("HEAD"))
		private void startBlockUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "runNext", at = @At("TAIL"))
		private void endBlockUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
		}
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.level.redstone.CollectingNeighborUpdater$FullNeighborUpdate")
	public static class StatefulEntryMixin
	{
		@Shadow @Final private Block block;
		@Shadow @Final private BlockPos pos;

		@Inject(method = "runNext", at = @At("HEAD"))
		private void startBlockUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "runNext", at = @At("TAIL"))
		private void endBlockUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			MicroTimingLoggerManager.onBlockUpdate(world, this.pos, this.block, BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END);
		}
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.level.redstone.CollectingNeighborUpdater$MultiNeighborUpdate")
	public static class SixWayEntryMixin
	{
		@Shadow @Final private Block sourceBlock;
		@Shadow @Final private BlockPos sourcePos;
		@Shadow @Final private Direction skipDirection;

		private boolean hasTriggeredStartEvent$TISCM = false;

		@Inject(method = "runNext", at = @At("HEAD"))
		private void startBlockUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			if (!this.hasTriggeredStartEvent$TISCM)
			{
				this.hasTriggeredStartEvent$TISCM = true;
				if (this.skipDirection == null)
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.sourcePos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START);
				}
				else
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.sourcePos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, this.skipDirection, EventType.ACTION_START);
				}
			}
		}

		@Inject(method = "runNext", at = @At("TAIL"))
		private void endBlockUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			if (!cir.getReturnValue())  // it's finished, returning false
			{
				if (this.skipDirection == null)
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.sourcePos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END);
				}
				else
				{
					MicroTimingLoggerManager.onBlockUpdate(world, this.sourcePos, this.sourceBlock, BlockUpdateType.BLOCK_UPDATE_EXCEPT, this.skipDirection, EventType.ACTION_END);
				}
			}
		}
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(targets = "net.minecraft.world.level.redstone.CollectingNeighborUpdater$ShapeUpdate")
	public static class StateReplacementEntryMixin
	{
		@Shadow @Final private BlockState
				//#if MC >= 1.21.2
				//$$ neighborState;
				//#else
				state;
				//#endif

		@Shadow @Final private BlockPos neighborPos;

		@Inject(method = "runNext", at = @At("HEAD"))
		private void startStateUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			//#if MC >= 1.21.2
			//$$ Block sourceBlock = this.neighborState.getBlock();
			//#else
			Block sourceBlock = this.state.getBlock();
			//#endif
			MicroTimingLoggerManager.onBlockUpdate(world, this.neighborPos, sourceBlock, BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_START);
		}

		@Inject(method = "runNext", at = @At("TAIL"))
		private void endStateUpdate(Level world, CallbackInfoReturnable<Boolean> cir)
		{
			//#if MC >= 1.21.2
			//$$ Block sourceBlock = this.neighborState.getBlock();
			//#else
			Block sourceBlock = this.state.getBlock();
			//#endif
			MicroTimingLoggerManager.onBlockUpdate(world, this.neighborPos, sourceBlock, BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_END);
		}
	}
}