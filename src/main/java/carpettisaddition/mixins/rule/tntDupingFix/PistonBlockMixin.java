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

package carpettisaddition.mixins.rule.tntDupingFix;

import carpettisaddition.CarpetTISAdditionSettings;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

//#if MC < 11500
//$$ import com.google.common.collect.Maps;
//$$ import java.util.Set;
//#endif

@Mixin(PistonBaseBlock.class)
public abstract class PistonBlockMixin
{
	@Inject(method = "moveBlocks", at = @At("HEAD"))
	private void storeRuleValueInCaseItChanges(CallbackInfoReturnable<Boolean> cir, @Share("isDupeFixed") LocalBooleanRef isDupeFixed)
	{
		// just in case the rule gets changed halfway
		isDupeFixed.set(CarpetTISAdditionSettings.tntDupingFix);
	}

	/**
	 * Set all blocks to be moved to air without any kind of update first (yeeted attached block updater like dead coral),
	 * then let vanilla codes to set the air blocks into b36
	 * Before setting a block to air, store the block state right before setting it to air to make sure no block desync
	 * will happen (yeeted onRemoved block updater like lit observer).
	 */
	@Inject(
			method = "moveBlocks",
			slice = @Slice(
					from = @At(
							value = "INVOKE",
							//#if MC >= 11700
							//$$ target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z"
							//#else
							target = "Lnet/minecraft/world/level/block/Block;isEntityBlock()Z"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Ljava/util/List;size()I",
					shift = At.Shift.AFTER,  // to make sure this will be injected after onMove in PistonBlock_movableTEMixin in fabric-carpet
					ordinal = 0
			)
	)
	private void setAllToBeMovedBlockToAirFirst(
			Level world, BlockPos pos, Direction dir, boolean retract,
			CallbackInfoReturnable<Boolean> cir,
			//#if MC >= 11500
			@Local Map<BlockPos, BlockState> map,
			//#endif
			@Local(ordinal = 0) List<BlockPos> list,  // pistonHandler.getMovedBlocks()
			@Local(ordinal = 1) List<BlockState> list2,   // states of list
			@Share("isDupeFixed") LocalBooleanRef isDupeFixed
	)
	{
		if (isDupeFixed.get())
		{
			// vanilla iterating order
			for (int l = list.size() - 1; l >= 0; --l)
			{
				BlockPos toBeMovedBlockPos = list.get(l);
				// Get the current state to make sure it is the state we want
				BlockState toBeMovedBlockState = world.getBlockState(toBeMovedBlockPos);
				// 68 is vanilla flag, 68 = 4 | 64
				// Added 16 to the vanilla flag, resulting in no block update or state update
				// Added 2 to the vanilla flag, so at those pos where will be air the listeners can be updated correctly
				// Although this cannot yeet onRemoved updaters, but it can prevent attached blocks from breaking,
				// which is nicer than just let them break imo
				world.setBlock(toBeMovedBlockPos, Blocks.AIR.defaultBlockState(), 2 | 4 | 16 | 64);

				// Update containers which contain the old state
				list2.set(l, toBeMovedBlockState);

				// map stores block pos and block state of moved blocks which changed into air due to block being moved
				//#if MC >= 11500
				map.put(toBeMovedBlockPos, toBeMovedBlockState);
				//#endif
			}
		}
	}

	/**
	 * Just to make sure blockStates array contains the correct values
	 * But ..., when reading states from it, mojang itself inverts the order and reads the wrong state releative to the blockpos
	 * When assigning:
	 *   blockStates = (list concat with list3 in order).map(world::getBlockState)
	 * When reading:
	 *   match list3[list3.size()-1] with blockStates[0]
	 *   match list3[list3.size()-2] with blockStates[1]
	 *   ...
	 * The block pos matches wrongly with block state, so mojang uses the wrong block as the source block to emit block updates :thonk:
	 * EDITED in 1.16.4: mojang has fixed it now
	 * <p>
	 * Whatever, just make it behave like vanilla
	 */
	@Inject(
			method = "moveBlocks",
			slice = @Slice(
					from = @At(
							value = "FIELD",
							//#if MC >= 11600
							//$$ target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isSticky:Z"
							//#else
							target = "Lnet/minecraft/world/level/block/piston/PistonBaseBlock;isSticky:Z"
							//#endif
					)
			),
			at = @At(
					value = "INVOKE",
					//#if MC >= 11500
					target = "Ljava/util/Map;keySet()Ljava/util/Set;",
					//#else
					//$$ target = "Ljava/util/Set;iterator()Ljava/util/Iterator;",
					//#endif
					ordinal = 0
			)
	)
	private void makeSureStatesInBlockStatesIsCorrect(
			Level world, BlockPos pos, Direction dir, boolean retract,
			CallbackInfoReturnable<Boolean> cir,
			@Local(ordinal = 0) List<BlockPos> list,  // pistonHandler.getMovedBlocks()
			@Local(ordinal = 1) List<BlockState> list2,  // states of list
			//#if MC >= 11600
			//$$ @Local(ordinal = 2) List<BlockPos> list3,  // pistonHandler.getBrokenBlocks()
			//#endif
			@Local BlockState[] blockStates,
			@Local(ordinal = 0) int j,
			//#if MC < 11500
			//$$ @Local(ordinal = 0) Set<BlockPos> set,
			//#endif
			@Share("isDupeFixed") LocalBooleanRef isDupeFixed
	)
	{
		if (isDupeFixed.get())
		{
			// since blockState8 = world.getBlockState(blockPos4) always return AIR due to the changes above
			// some states value in blockStates array need to be corrected
			// list and list2 has the same size and indicating the same block

			//#if MC >= 11600
			//$$ int j2 = list3.size();
			//#else
			int j2 = j + list.size();
			//#endif

			for (int l2 = list.size() - 1; l2 >= 0; --l2)
			{
				//#if MC >= 11600
				//$$ blockStates[j2++] = list2.get(l2);
				//#else
				--j2;
				blockStates[j2] = list2.get(l2);
				//#endif
			}


			//#if MC < 11500
			//$$ // Emit missing state updates at set positions manually
			//$$ Map<BlockPos, BlockState> stateMap = Maps.newHashMap();
			//$$ for (int i = 0; i < list.size(); i++)
			//$$ {
			//$$ 	stateMap.put(list.get(i), list2.get(i));
			//$$ }
			//$$
			//$$ for (BlockPos blockPos1 : set)
			//$$ {
			//$$ 	BlockState blockState1 = stateMap.get(blockPos1);
			//$$ 	blockState1.updateNeighbourShapes(world, blockPos1, 68 & -2);
			//$$ }
			//#endif
		}
	}
}
