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
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.DoubleInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//#if MC >= 11500
import net.minecraft.block.DoubleBlockProperties;
//#endif

public class LargeBarrelHelper
{
	/**
	 * We want to have barrels facing on the same axis to be connected, but the 2 barrels will have opposite direction,
	 * which cannot pass the "blockState.get(directionProperty) == state.get(directionProperty)" test in {@link DoubleBlockProperties#toPropertySource)}
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

	public static DoubleBlockProperties.PropertySource<? extends BarrelBlockEntity> getBlockEntitySource(BlockState blockState, World world, BlockPos pos) {
		gettingLargeBarrelPropertySource.set(true);
		try
		{
			return DoubleBlockProperties.toPropertySource(
					BlockEntityType.BARREL,
					state -> state.get(BarrelBlock.FACING).getDirection() == Direction.AxisDirection.NEGATIVE ? DoubleBlockProperties.Type.FIRST : DoubleBlockProperties.Type.SECOND,
					state -> state.get(BarrelBlock.FACING).getOpposite(),
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
	 * Just like {@link net.minecraft.block.ChestBlock#getInventory}
	 */
	@Nullable
	public static Inventory getInventory(BlockState state, World world, BlockPos pos)
	{
		return getBlockEntitySource(state, world, pos).apply(LargeBarrelHelper.INVENTORY_RETRIEVER).orElse(null);
	}

	public static boolean isLargeBarrel(BlockState state, World world, BlockPos pos)
	{
		return getOtherPos(state, world, pos) != null;
	}

	@Nullable
	public static BlockPos getOtherPos(BlockState state, World world, BlockPos pos)
	{
		if (state.getBlock() instanceof BarrelBlock)
		{
			Direction facing = state.get(BarrelBlock.FACING);
			BlockPos otherPos = pos.offset(facing.getOpposite());
			BlockState otherState = world.getBlockState(otherPos);
			if (otherState.getBlock() instanceof BarrelBlock)
			{
				Direction otherFacing = otherState.get(BarrelBlock.FACING);
				if (otherFacing == facing.getOpposite())
				{
					return otherPos;
				}
			}
		}
		return null;
	}

	/**
	 * INVENTORY_RETRIEVER and NAME_RETRIEVER are totally not stolen from {@link net.minecraft.block.ChestBlock} XD
	 */

	public static final DoubleBlockProperties.PropertyRetriever<BarrelBlockEntity, Optional<Inventory>> INVENTORY_RETRIEVER = new DoubleBlockProperties.PropertyRetriever<BarrelBlockEntity, Optional<Inventory>>()
	{
		public Optional<Inventory> getFromBoth(BarrelBlockEntity BarrelBlockEntity, BarrelBlockEntity BarrelBlockEntity2)
		{
			return Optional.of(new DoubleInventory(BarrelBlockEntity, BarrelBlockEntity2));
		}

		public Optional<Inventory> getFrom(BarrelBlockEntity BarrelBlockEntity)
		{
			return Optional.of(BarrelBlockEntity);
		}

		public Optional<Inventory> getFallback()
		{
			return Optional.empty();
		}
	};

	public static final DoubleBlockProperties.PropertyRetriever<BarrelBlockEntity, Optional<NameableContainerFactory>> NAME_RETRIEVER = new DoubleBlockProperties.PropertyRetriever<BarrelBlockEntity, Optional<NameableContainerFactory>>()
	{
		public Optional<NameableContainerFactory> getFromBoth(BarrelBlockEntity barrel1, BarrelBlockEntity barrel2)
		{
			final Inventory inventory = new DoubleInventory(barrel1, barrel2);
			return Optional.of(new NameableContainerFactory()
			{
				@Nullable
				@Override
				public Container createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity playerEntity)
				{
					if (barrel1.checkUnlocked(playerEntity) && barrel2.checkUnlocked(playerEntity))
					{
						barrel1.checkLootInteraction(playerInventory.player);
						barrel2.checkLootInteraction(playerInventory.player);
						return GenericContainer.createGeneric9x6(syncId, playerInventory, inventory);
					}
					else
					{
						return null;
					}
				}

				@Override
				public Text getDisplayName()
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

		public Optional<NameableContainerFactory> getFrom(BarrelBlockEntity barrelBlockEntity)
		{
			return Optional.of(barrelBlockEntity);
		}

		public Optional<NameableContainerFactory> getFallback()
		{
			return Optional.empty();
		}
	};
}
