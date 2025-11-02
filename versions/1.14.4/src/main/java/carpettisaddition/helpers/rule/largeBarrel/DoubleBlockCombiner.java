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

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Totally stolen from 1.15.2 for easier porting to 1.14.4
 */
public class DoubleBlockCombiner {
	/**
	 * Stolen from 1.15.2 net.minecraft.world.level.block.entity.BlockEntityType#getBlockEntity
	 */
	@SuppressWarnings("unchecked")
	private static <S extends BlockEntity> S getBlockEntity(BlockEntityType<S> blockEntityType, BlockGetter world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (CarpetTISAdditionSettings.largeBarrel && LargeBarrelHelper.enabledOffThreadBlockEntityAccess.get())
		{
			if (world instanceof Level)
			{
				blockEntity = ((Level)world).getChunk(pos).getBlockEntity(pos);
			}
		}
		return blockEntity != null && blockEntity.getType() == blockEntityType ? (S)blockEntity : null;
	}

	public static <S extends BlockEntity> DoubleBlockCombiner.NeighborCombineResult<S> combineWithNeigbour(
			BlockEntityType<S> blockEntityType,
			Function<BlockState, DoubleBlockCombiner.BlockType> function,
			Function<BlockState, Direction> function2,
			DirectionProperty directionProperty,
			BlockState blockState,
			LevelAccessor levelAccessor,
			BlockPos blockPos,
			BiPredicate<LevelAccessor, BlockPos> biPredicate
	) {
		S blockEntity = getBlockEntity(blockEntityType, levelAccessor, blockPos);
		if (blockEntity == null) {
			return DoubleBlockCombiner.Combiner::acceptNone;
		} else if (biPredicate.test(levelAccessor, blockPos)) {
			return DoubleBlockCombiner.Combiner::acceptNone;
		} else {
			DoubleBlockCombiner.BlockType blockType = function.apply(blockState);
			boolean bl = blockType == DoubleBlockCombiner.BlockType.SINGLE;
			boolean bl2 = blockType == DoubleBlockCombiner.BlockType.FIRST;
			if (bl) {
				return new DoubleBlockCombiner.NeighborCombineResult.Single<>(blockEntity);
			} else {
				BlockPos blockPos2 = blockPos.relative(function2.apply(blockState));
				BlockState blockState2 = levelAccessor.getBlockState(blockPos2);
				if (blockState2.getBlock() == blockState.getBlock()) {
					DoubleBlockCombiner.BlockType blockType2 = function.apply(blockState2);
					if (blockType2 != DoubleBlockCombiner.BlockType.SINGLE
							&& blockType != blockType2
							&& blockState2.getValue(directionProperty) == blockState.getValue(directionProperty)) {
						if (biPredicate.test(levelAccessor, blockPos2)) {
							return DoubleBlockCombiner.Combiner::acceptNone;
						}

						S blockEntity2 = getBlockEntity(blockEntityType, levelAccessor, blockPos2);
						if (blockEntity2 != null) {
							S blockEntity3 = bl2 ? blockEntity : blockEntity2;
							S blockEntity4 = bl2 ? blockEntity2 : blockEntity;
							return new DoubleBlockCombiner.NeighborCombineResult.Double<>(blockEntity3, blockEntity4);
						}
					}
				}

				return new DoubleBlockCombiner.NeighborCombineResult.Single<>(blockEntity);
			}
		}
	}

	public static enum BlockType {
		SINGLE,
		FIRST,
		SECOND;
	}

	public interface Combiner<S, T> {
		T acceptDouble(S object, S object2);

		T acceptSingle(S object);

		T acceptNone();
	}

	public interface NeighborCombineResult<S> {
		<T> T apply(DoubleBlockCombiner.Combiner<? super S, T> combiner);

		public static final class Double<S> implements DoubleBlockCombiner.NeighborCombineResult<S> {
			private final S first;
			private final S second;

			public Double(S object, S object2) {
				this.first = object;
				this.second = object2;
			}

			@Override
			public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> combiner) {
				return combiner.acceptDouble(this.first, this.second);
			}
		}

		public static final class Single<S> implements DoubleBlockCombiner.NeighborCombineResult<S> {
			private final S single;

			public Single(S object) {
				this.single = object;
			}

			@Override
			public <T> T apply(DoubleBlockCombiner.Combiner<? super S, T> combiner) {
				return combiner.acceptSingle(this.single);
			}
		}
	}
}
