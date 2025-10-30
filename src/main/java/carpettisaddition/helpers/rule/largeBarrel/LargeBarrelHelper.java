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

package carpettisaddition.helpers.rule.largeBarrel;

import carpettisaddition.utils.Messenger;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//#if MC >= 11500
import net.minecraft.world.level.block.DoubleBlockCombiner;
//#endif

public class LargeBarrelHelper
{
	/**
	 * We want to have barrels facing on the same axis to be connected, but the 2 barrels will have opposite direction,
	 * which cannot pass the "blockState2.getValue(directionProperty) == blockState.getValue(directionProperty)" test in {@link DoubleBlockCombiner#combineWithNeigbour)}
	 * This global flag is for enabling the mixin
	 *   {@link carpettisaddition.mixins.rule.largeBarrel.DoubleBlockPropertiesMixin} and
	 *   {@link carpettisaddition.mixins.rule.largeBarrel.AbstractStateMixin}
	 * to ignore the axis direction of the BarrelBlock.FACING property of the barrel block
	 */
	public static final ThreadLocal<Boolean> gettingLargeBarrelPropertySource = ThreadLocal.withInitial(() -> false);
	public static final ThreadLocal<Boolean> applyAxisOnlyDirectionTesting = ThreadLocal.withInitial(() -> false);

	/**
	 * World.getBlockEntity does not support off-server-thread accessing
	 * When we want to get the large barrel inventory in single player via the integrated server world
	 * on client Render thread, we need to somehow disable the off-thread testing
	 *
	 * Here's the flag for enable the functionality of mixin
	 * {@link carpettisaddition.mixins.rule.largeBarrel.compat.malilib.BlockEntityTypeMixin}
	 */
	public static final ThreadLocal<Boolean> enabledOffThreadBlockEntityAccess = ThreadLocal.withInitial(() -> false);

	public static DoubleBlockCombiner.NeighborCombineResult<? extends BarrelBlockEntity> getBlockEntitySource(BlockState blockState, Level world, BlockPos pos) {
		gettingLargeBarrelPropertySource.set(true);
		try
		{
			return DoubleBlockCombiner.combineWithNeigbour(
					BlockEntityType.BARREL,
					state -> state.getValue(BarrelBlock.FACING).getAxisDirection() == Direction.AxisDirection.NEGATIVE ? DoubleBlockCombiner.BlockType.FIRST : DoubleBlockCombiner.BlockType.SECOND,
					state -> state.getValue(BarrelBlock.FACING).getOpposite(),
					BarrelBlock.FACING,
					blockState, world, pos,
					(iWorld, blockPos) -> false
			);
		}
		finally
		{
			gettingLargeBarrelPropertySource.set(false);
			applyAxisOnlyDirectionTesting.set(false); // just in case
		}
	}

	/**
	 * Just like {@link net.minecraft.world.level.block.ChestBlock#getInventory}
	 */
	@Nullable
	public static Container getInventory(BlockState state, Level world, BlockPos pos)
	{
		return getBlockEntitySource(state, world, pos).apply(LargeBarrelHelper.INVENTORY_RETRIEVER).orElse(null);
	}

	public static boolean isLargeBarrel(BlockState state, Level world, BlockPos pos)
	{
		return getOtherPos(state, world, pos) != null;
	}

	@Nullable
	public static BlockPos getOtherPos(BlockState state, Level world, BlockPos pos)
	{
		if (state.getBlock() instanceof BarrelBlock)
		{
			Direction facing = state.getValue(BarrelBlock.FACING);
			BlockPos otherPos = pos.relative(facing.getOpposite());
			BlockState otherState = world.getBlockState(otherPos);
			if (otherState.getBlock() instanceof BarrelBlock)
			{
				Direction otherFacing = otherState.getValue(BarrelBlock.FACING);
				if (otherFacing == facing.getOpposite())
				{
					return otherPos;
				}
			}
		}
		return null;
	}

	/**
	 * INVENTORY_RETRIEVER and NAME_RETRIEVER are totally not stolen from {@link net.minecraft.world.level.block.ChestBlock} XD
	 */

	public static final DoubleBlockCombiner.Combiner<BarrelBlockEntity, Optional<Container>> INVENTORY_RETRIEVER = new DoubleBlockCombiner.Combiner<BarrelBlockEntity, Optional<Container>>()
	{
		public Optional<Container> acceptDouble(BarrelBlockEntity BarrelBlockEntity, BarrelBlockEntity BarrelBlockEntity2)
		{
			return Optional.of(new CompoundContainer(BarrelBlockEntity, BarrelBlockEntity2));
		}

		public Optional<Container> acceptSingle(BarrelBlockEntity BarrelBlockEntity)
		{
			return Optional.of(BarrelBlockEntity);
		}

		public Optional<Container> acceptNone()
		{
			return Optional.empty();
		}
	};

	public static final DoubleBlockCombiner.Combiner<BarrelBlockEntity, Optional<MenuProvider>> NAME_RETRIEVER = new DoubleBlockCombiner.Combiner<BarrelBlockEntity, Optional<MenuProvider>>()
	{
		public Optional<MenuProvider> acceptDouble(BarrelBlockEntity barrel1, BarrelBlockEntity barrel2)
		{
			final Container inventory = new CompoundContainer(barrel1, barrel2);
			return Optional.of(new MenuProvider()
			{
				@Nullable
				@Override
				public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player playerEntity)
				{
					if (barrel1.canOpen(playerEntity) && barrel2.canOpen(playerEntity))
					{
						barrel1.unpackLootTable(playerInventory.player);
						barrel2.unpackLootTable(playerInventory.player);
						return ChestMenu.sixRows(syncId, playerInventory, inventory);
					}
					else
					{
						return null;
					}
				}

				@Override
				public Component getDisplayName()
				{
					if (barrel1.hasCustomName())
					{
						return barrel1.getDisplayName();
					}
					else if (barrel2.hasCustomName())
					{
						return barrel2.getDisplayName();
					}
					return Messenger.tr("container.barrel");
//					return Messenger.s("Large Barrel");
				}
			});
		}

		public Optional<MenuProvider> acceptSingle(BarrelBlockEntity barrelBlockEntity)
		{
			return Optional.of(barrelBlockEntity);
		}

		public Optional<MenuProvider> acceptNone()
		{
			return Optional.empty();
		}
	};
}
