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

package carpettisaddition.mixins.rule.largeBarrel.compat.lithium;

import carpettisaddition.CarpetTISAdditionSettings;
import carpettisaddition.utils.ModIds;
import com.llamalad7.mixinextras.sugar.Local;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import me.jellysquid.mods.lithium.common.hopper.RemovableBlockEntity;
import me.jellysquid.mods.lithium.common.world.blockentity.BlockEntityGetter;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

//#if MC >= 12105
//$$ import net.minecraft.server.level.ServerLevel;
//#endif

@Restriction(require = {
		@Condition(value = ModIds.minecraft, versionPredicates = ">=1.17"),
		@Condition(ModIds.lithium)
})
@Mixin(value = BarrelBlock.class, priority = 2000)
public abstract class BarrelBlockMixin extends BaseEntityBlock
{
	protected BarrelBlockMixin(Settings settings)
	{
		super(settings);
	}

	/**
	 * aka "block.hopper" optimization in lithium
	 * reference:
	 * - lithium < 0.9.0: {@link me.jellysquid.mods.lithium.mixin.block.hopper.BlockEntityMixin}
	 * - lithium >= 0.9.0 (>=1.19): {@link me.jellysquid.mods.lithium.mixin.util.inventory_change_listening.BlockEntityMixin}
	 */
	@Unique
	private static final boolean LITHIUM_HOPPER_OPTIMIZATION_LOADED = RemovableBlockEntity.class.isAssignableFrom(BarrelBlockEntity.class);

	@SuppressWarnings("deprecation")
	@Override
	public void onBlockAdded(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved)
	{
		super.onBlockAdded(state, world, pos, oldState, moved);
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			resetLithiumHopperCache(world, pos, state);
		}
	}

	@ModifyVariable(
			method = "onStateReplaced",
			at = @At("TAIL"),
			argsOnly = true
	)
	//#if MC >= 12105
	//$$ public ServerLevel resetLithiumHopperCacheForLargeBarrel(
	//$$ ServerLevel world,
	//#else
	public Level resetLithiumHopperCacheForLargeBarrel(
			Level world,
	//#endif
			@Local(argsOnly = true, ordinal = 0) BlockState state,
			@Local(argsOnly = true)  BlockPos pos
	)
	{
		if (CarpetTISAdditionSettings.largeBarrel)
		{
			resetLithiumHopperCache(world, pos, state);
		}
		return world;
	}

	@Unique
	private static void resetLithiumHopperCache(LevelAccessor world, BlockPos changedBarrelPos, BlockState changedBarrelState)
	{
		if (LITHIUM_HOPPER_OPTIMIZATION_LOADED && !world.isClient())
		{
			Direction changedBarrelDirection = changedBarrelState.get(BarrelBlock.FACING);
			BlockPos affectedBarrelPos = changedBarrelPos.offset(changedBarrelDirection.getOpposite());
			BlockState affectedBarrelState = world.getBlockState(affectedBarrelPos);
			if (affectedBarrelState.getBlock() instanceof BarrelBlock && affectedBarrelState.get(BarrelBlock.FACING) == changedBarrelDirection.getOpposite())
			{
				BlockEntity barrelBlockEntity = ((BlockEntityGetter)world).getLoadedExistingBlockEntity(affectedBarrelPos);
				if (barrelBlockEntity instanceof BarrelBlockEntity)
				{
					// let lithium re-calculate the target inventory
					// - (< mc1.20.5) via HopperHelper#vanillaGetBlockInventory
					// - (>= mc1.20.5) directly using HopperBlockEntity#getBlockInventoryAt
					// we have a nice mixin injection there to handle largeBarrel like vanilla
					((RemovableBlockEntity)barrelBlockEntity).increaseRemoveCounter();
				}
			}
		}
	}
}