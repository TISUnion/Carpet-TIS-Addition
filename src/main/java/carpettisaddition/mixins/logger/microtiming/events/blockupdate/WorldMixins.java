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
import carpettisaddition.logging.loggers.microtiming.utils.MicroTimingUtil;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.block.Block;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//#if MC >= 11900
//$$ import net.minecraft.block.BlockState;
//#endif

@SuppressWarnings("ConstantConditions")
public abstract class WorldMixins
{
	@Mixin(
			//#if MC >= 11900
			//$$ ServerWorld.class
			//#else
			World.class
			//#endif
	)
	public static class BlockUpdateMixin
	{
		@Inject(
				//#if MC >= 12102
				//$$ method = "updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;)V",
				//#else
				method = "updateNeighborsAlways",
				//#endif
				at = @At("HEAD")
		)
		private void startUpdateNeighborsAlways(CallbackInfo ci, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Block block)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, pos, block,
						BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_START
				);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate(
						(World)(Object)this, pos, block,
						BlockUpdateType.BLOCK_UPDATE, null
				);
			}
		}

		@Inject(
				//#if MC >= 12102
				//$$ method = "updateNeighborsAlways(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;)V",
				//#else
				method = "updateNeighborsAlways",
				//#endif
				at = @At("RETURN")
		)
		private void endUpdateNeighborsAlways(CallbackInfo ci, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Block block)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, pos, block,
						BlockUpdateType.BLOCK_UPDATE, null, EventType.ACTION_END
				);
			}
		}
	}

	@Mixin(
			//#if MC >= 11900
			//$$ ServerWorld.class
			//#else
			World.class
			//#endif
	)
	public static class BlockUpdateExceptMixin
	{
		// TODO: support logging WireOrientation in mc 1.21.2+

		@Inject(method = "updateNeighborsExcept", at = @At("HEAD"))
		private void startUpdateNeighborsExcept(
				CallbackInfo ci,
				@Local(argsOnly = true) BlockPos pos,
				@Local(argsOnly = true) Block sourceBlock,
				@Local(argsOnly = true) Direction direction
		)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, pos, sourceBlock,
						BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_START
				);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate(
						(World)(Object)this, pos, sourceBlock,
						BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction
				);
			}
		}

		@Inject(method = "updateNeighborsExcept", at = @At("RETURN"))
		private void endUpdateNeighborsExcept(
				CallbackInfo ci,
				@Local(argsOnly = true) BlockPos pos,
				@Local(argsOnly = true) Block sourceBlock,
				@Local(argsOnly = true) Direction direction
		)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, pos, sourceBlock,
						BlockUpdateType.BLOCK_UPDATE_EXCEPT, direction, EventType.ACTION_END
				);
			}
		}
	}

	@Mixin(World.class)
	public static class ComparatorUpdateMixin
	{
		@Inject(
				//#if MC >= 11600
				//$$ method = "updateComparators",
				//#else
				method = "updateHorizontalAdjacent",
				//#endif
				at = @At("HEAD")
		)
		private void startUpdateComparator(BlockPos pos, Block block, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, pos, block,
						BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_START
				);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate(
						(World)(Object)this, pos, block,
						BlockUpdateType.COMPARATOR_UPDATE, null
				);
			}
		}

		@Inject(
				//#if MC >= 11600
				//$$ method = "updateComparators",
				//#else
				method = "updateHorizontalAdjacent",
				//#endif
				at = @At("RETURN")
		)
		private void endUpdateComparator(BlockPos pos, Block block, CallbackInfo ci)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, pos, block,
						BlockUpdateType.COMPARATOR_UPDATE, null, EventType.ACTION_END
				);
			}
		}
	}

	@Mixin(
			//#if MC >= 11900
			//$$ ServerWorld.class
			//#else
			World.class
			//#endif
	)
	public static class SingleBlockUpdateMixin
	{
		@Inject(
				//#if MC >= 12102
				//$$ method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;)V",
				//#else
				method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V",
				//#endif
				at = @At("HEAD")
		)
		private void startUpdateSingleBlock(CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) BlockPos sourcePos, @Local(argsOnly = true) Block sourceBlock)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, sourcePos, sourceBlock,
						BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START
				);
			}
			else
			{
				MicroTimingLoggerManager.onScheduleBlockUpdate(
						(World)(Object)this, sourcePos, sourceBlock,
						BlockUpdateType.SINGLE_BLOCK_UPDATE, null
				);
			}
		}

		@Inject(
				//#if MC >= 12102
				//$$ method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;)V",
				//#else
				method = "updateNeighbor(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;)V",
				//#endif
				at = @At("TAIL")
		)
		private void endUpdateSingleBlock(CallbackInfo ci, @Local(argsOnly = true, ordinal = 0) BlockPos sourcePos, @Local(argsOnly = true) Block sourceBlock)
		{
			if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
			{
				MicroTimingLoggerManager.onBlockUpdate(
						(World)(Object)this, sourcePos, sourceBlock,
						BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END
				);
			}
		}
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(ServerWorld.class)
	public static class SingleBlockUpdate2Mixin
	{
		//#if MC >= 11900
		//$$ @Inject(
		//$$ 		//#if MC >= 12102
		//$$ 		//$$ method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;Z)V",
		//$$ 		//#else
		//$$ 		method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V",
		//$$ 		//#endif
		//$$ 		at = @At("HEAD")
		//$$ )
		//$$ private void startUpdateSingleBlock2(
		//$$ 		CallbackInfo ci,
		//$$ 		@Local(argsOnly = true, ordinal = 0) BlockPos blockPos,
		//$$ 		@Local(argsOnly = true) Block block
		//$$ )
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate(
		//$$ 				(World)(Object)this, blockPos, block,
		//$$ 				BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_START
		//$$ 		);
		//$$ 	}
		//$$ 	else
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onScheduleBlockUpdate(
		//$$ 				(World)(Object)this, blockPos, block,
		//$$ 				BlockUpdateType.SINGLE_BLOCK_UPDATE, null
		//$$ 		);
		//$$ 	}
		//$$ }
  //$$
		//$$ @Inject(
		//$$ 		//#if MC >= 12102
		//$$ 		//$$ method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/world/block/WireOrientation;Z)V",
		//$$ 		//#else
		//$$ 		method = "updateNeighbor(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V",
		//$$ 		//#endif
		//$$ 		at = @At("TAIL")
		//$$ )
		//$$ private void endUpdateSingleBlock2(
		//$$ 		CallbackInfo ci,
		//$$ 		@Local(argsOnly = true, ordinal = 0) BlockPos blockPos,
		//$$ 		@Local(argsOnly = true) Block block
		//$$ )
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate(
		//$$ 				(World)(Object)this, blockPos, block,
		//$$ 				BlockUpdateType.SINGLE_BLOCK_UPDATE, null, EventType.ACTION_END
		//$$ 		);
		//$$ 	}
		//$$ }
		//#endif
	}

	@Restriction(require = @Condition(value = ModIds.minecraft, versionPredicates = ">=1.19"))
	@Mixin(World.class)
	public static class SingleStateUpdateMixin
	{
		//#if MC >= 11900
		//$$ @Inject(method = "replaceWithStateForNeighborUpdate", at = @At("HEAD"))
		//$$ private void startStateUpdateSingleBlock(
		//$$ 		CallbackInfo ci,
		//$$ 		@Local(argsOnly = true) BlockState blockState,
		//$$ 		@Local(argsOnly = true, ordinal = 1) BlockPos sourcePos
		//$$ )
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate(
		//$$ 				(World)(Object)this, sourcePos, blockState.getBlock(),
		//$$ 				BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_START
		//$$ 		);
		//$$ 	}
		//$$ 	else
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onScheduleBlockUpdate(
		//$$ 				(World)(Object)this, sourcePos, blockState.getBlock(),
		//$$ 				BlockUpdateType.SINGLE_STATE_UPDATE, null
		//$$ 		);
		//$$ 	}
		//$$ }
		//$$ @Inject(method = "replaceWithStateForNeighborUpdate", at = @At("HEAD"))
		//$$ private void endStateUpdateSingleBlock(
		//$$ 		CallbackInfo ci,
		//$$ 		@Local(argsOnly = true) BlockState blockState,
		//$$ 		@Local(argsOnly = true, ordinal = 1) BlockPos sourcePos
		//$$ )
		//$$ {
		//$$ 	if (MicroTimingUtil.isBlockUpdateInstant((World)(Object)this))
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onBlockUpdate(
		//$$ 				(World)(Object)this, sourcePos, blockState.getBlock(),
		//$$ 				BlockUpdateType.SINGLE_STATE_UPDATE, null, EventType.ACTION_END
		//$$ 		);
		//$$ 	}
		//$$ }
		//#endif
	}
}
