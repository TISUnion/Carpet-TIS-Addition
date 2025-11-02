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

package carpettisaddition.mixins.logger.microtiming.events;

import carpettisaddition.logging.loggers.microtiming.MicroTimingLoggerManager;
import carpettisaddition.logging.loggers.microtiming.enums.EventType;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.TripWireHookBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//#if MC >= 12102
//$$ import net.minecraft.world.level.redstone.DefaultRedstoneWireEvaluator;
//#endif

//#if MC >= 11600
//$$ import java.util.Iterator;
//$$ import java.util.Set;
//#else
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import java.util.List;
//#endif

public abstract class EmitBlockUpdateMixins
{
	@Mixin(DiodeBlock.class)
	public static abstract class AbstractRedstoneGateBlockMixin
	{
		@Inject(method = "updateNeighborsInFront", at = @At("HEAD"))
		private void startEmitBlockUpdate(Level world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (DiodeBlock)(Object)this, pos, EventType.ACTION_START, "updateTarget");
		}

		@Inject(method = "updateNeighborsInFront", at = @At("RETURN"))
		private void endEmitBlockUpdate(Level world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (DiodeBlock)(Object)this, pos, EventType.ACTION_END, "updateTarget");
		}
	}

	@Mixin(ObserverBlock.class)
	public static abstract class ObserverBlockMixin
	{
		@Inject(method = "updateNeighborsInFront", at = @At("HEAD"))
		private void startEmitBlockUpdate(Level world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (ObserverBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighbors");
		}

		@Inject(method = "updateNeighborsInFront", at = @At("RETURN"))
		private void endEmitBlockUpdate(Level world, BlockPos pos, BlockState state, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (ObserverBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighbors");
		}
	}

	@Mixin(PoweredRailBlock.class)
	public static abstract class PoweredRailBlockMixin
	{
		@Inject(
				method = "updateState",
				at = @At(
						value = "INVOKE",
						shift = At.Shift.AFTER,
						target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
				)
		)
		private void startEmitBlockUpdate(BlockState state, Level world, BlockPos pos, Block neighbor, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (PoweredRailBlock)(Object)this, pos, EventType.ACTION_START, "updateBlockState");
		}

		@Inject(
				method = "updateState",
				at = @At("RETURN"),
				locals = LocalCapture.CAPTURE_FAILHARD
		)
		private void endEmitBlockUpdate(BlockState state, Level world, BlockPos pos, Block neighbor, CallbackInfo ci, boolean bl, boolean bl2)
		{
			if (bl2 != bl)  // vanilla copy, if power state should be changed
			{
				MicroTimingLoggerManager.onEmitBlockUpdate(world, (PoweredRailBlock) (Object) this, pos, EventType.ACTION_END, "updateBlockState");
			}
		}
	}

	@Mixin(LeverBlock.class)
	public static abstract class LeverBlockMixin
	{
		@Inject(method = "updateNeighbours", at = @At("HEAD"))
		private void startEmitBlockUpdate(BlockState state, Level world, BlockPos pos, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (LeverBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighbors");
		}

		@Inject(method = "updateNeighbours", at = @At("RETURN"))
		private void endEmitBlockUpdate(BlockState state, Level world, BlockPos pos, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (LeverBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighbors");
		}
	}

	@Mixin(ButtonBlock.class)
	public static abstract class AbstractButtonBlockMixin
	{
		@Inject(method = "updateNeighbours", at = @At("HEAD"))
		private void startEmitBlockUpdate(BlockState state, Level world, BlockPos pos, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (ButtonBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighbors");
		}

		@Inject(method = "updateNeighbours", at = @At("RETURN"))
		private void endEmitBlockUpdate(BlockState state, Level world, BlockPos pos, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (ButtonBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighbors");
		}
	}

	@Mixin(BasePressurePlateBlock.class)
	public static abstract class AbstractPressurePlateBlockMixin
	{
		@Inject(method = "updateNeighbours", at = @At("HEAD"))
		private void startEmitBlockUpdate(Level world, BlockPos pos, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (BasePressurePlateBlock)(Object)this, pos, EventType.ACTION_START, "updateNeighbors");
		}

		@Inject(method = "updateNeighbours", at = @At("RETURN"))
		private void endEmitBlockUpdate(Level world, BlockPos pos, CallbackInfo ci)
		{
			MicroTimingLoggerManager.onEmitBlockUpdate(world, (BasePressurePlateBlock)(Object)this, pos, EventType.ACTION_END, "updateNeighbors");
		}
	}

	@Mixin(TripWireHookBlock.class)
	public static abstract class TripwireHookBlockMixin
	{
		@Inject(method = "notifyNeighbors", at = @At("HEAD"))
		private
		//#if MC >= 12003
		//$$ static
		//#endif
		void startEmitBlockUpdate(
				//#if MC >= 12003
				//$$ Block block,
				//#endif
				Level world, BlockPos pos, Direction direction, CallbackInfo ci
		)
		{
			//#if MC < 12003
			Block block = (TripWireHookBlock)(Object)this;
			//#endif
			MicroTimingLoggerManager.onEmitBlockUpdate(world, block, pos, EventType.ACTION_START, "updateNeighborsOnAxis");
		}

		@Inject(method = "notifyNeighbors", at = @At("RETURN"))
		private
		//#if MC >= 12003
		//$$ static
		//#endif
		void endEmitBlockUpdate(
				//#if MC >= 12003
				//$$ Block block,
				//#endif
				Level world, BlockPos pos, Direction direction, CallbackInfo ci
		)
		{
			//#if MC < 12003
			Block block = (TripWireHookBlock)(Object)this;
			//#endif
			MicroTimingLoggerManager.onEmitBlockUpdate(world, block, pos, EventType.ACTION_END, "updateNeighborsOnAxis");
		}
	}

	/**
	 * Use larger priority, so it injects after the @ModifyVariable in
	 * {@link carpettisaddition.mixins.rule.redstoneDustRandomUpdateOrder.RedstoneWireBlockMixin}
	 */
	@Mixin(
			//#if MC >= 12102
			//$$ value = DefaultRedstoneWireEvaluator.class,
			//#else
			value = RedStoneWireBlock.class,
			//#endif
			priority = 2000
	)
	public static abstract class RedstoneWireBlockMixin
	{
		// TODO: support ExperimentalRedstoneController in mc 1.21.2+

		@Inject(
				method = "updatePowerStrength",
				at = @At(
						value = "INVOKE",
						//#if MC >= 11600
						//$$ target = "Ljava/util/Set;iterator()Ljava/util/Iterator;"
						//#else
						target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
						//#endif
				)
		)
		private void startEmitBlockUpdate(
				//#if MC >= 11600
				//$$ CallbackInfo ci, @Local Set<BlockPos> collection,
				//#else
				CallbackInfoReturnable<BlockState> cir, @Local List<BlockPos> collection,
				//#endif
				//#if MC >= 12102
				//$$ @Local(argsOnly = true) BlockState state,
				//#endif
				@Local(argsOnly = true) Level world, @Local(argsOnly = true) BlockPos pos
		)
		{
			MicroTimingLoggerManager.onEmitBlockUpdateRedstoneDust(
					world,
					//#if MC >= 12102
					//$$ state.getBlock(),
					//#else
					(RedStoneWireBlock)(Object)this,
					//#endif
					pos, EventType.ACTION_START, "update", collection
			);
		}

		//#if MC >= 11600
		//$$ @Inject(
		//$$ 		method = "updatePowerStrength",
		//$$ 		at = @At(
		//$$ 				value = "INVOKE",
		//$$ 				target = "Ljava/util/Iterator;hasNext()Z"
		//$$ 		)
		//$$ )
		//$$ private void endEmitBlockUpdate(
		//$$ 		CallbackInfo ci,
		//$$ 		@Local(argsOnly = true) Level world,
		//$$ 		@Local(argsOnly = true) BlockPos pos,
		//$$ 		//#if MC >= 12102
		//$$ 		//$$ @Local(argsOnly = true) BlockState state,
		//$$ 		//#endif
		//$$ 		@Local Iterator<BlockPos> iterator
		//$$ )
		//$$ {
		//$$ 	if (!iterator.hasNext())
		//$$ 	{
		//$$ 		MicroTimingLoggerManager.onEmitBlockUpdateRedstoneDust(
		//$$ 				world,
		//$$ 				//#if MC >= 12102
		//$$ 				//$$ state.getBlock(),
		//$$ 				//#else
		//$$ 				(RedStoneWireBlock)(Object)this,
		//$$ 				//#endif
		//$$ 				pos, EventType.ACTION_END, "update", null
		//$$ 		);
		//$$ 	}
		//$$ }
		//#else
		@Inject(method = "updatePowerStrength", at = @At("RETURN"))
		private void endEmitBlockUpdate(Level world, BlockPos pos, BlockState state, CallbackInfoReturnable<BlockState> cir)
		{
			MicroTimingLoggerManager.onEmitBlockUpdateRedstoneDust(world, (RedStoneWireBlock)(Object)this, pos, EventType.ACTION_END, "update", null);
		}
		//#endif
	}
}
