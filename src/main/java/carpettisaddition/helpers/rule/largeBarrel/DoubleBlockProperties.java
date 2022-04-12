package carpettisaddition.helpers.rule.largeBarrel;

import carpettisaddition.CarpetTISAdditionSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * Totally stolen from 1.15.2 for easier porting
 */
public class DoubleBlockProperties {
	/**
	 * Stolen from 1.15.2 net.minecraft.block.entity.BlockEntityType#get
	 */
	@SuppressWarnings("unchecked")
	private static <S extends BlockEntity> S getBlockEntity(BlockEntityType<S> blockEntityType, BlockView world, BlockPos pos) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (CarpetTISAdditionSettings.largeBarrel && LargeBarrelHelper.enabledOffThreadBlockEntityAccess.get())
		{
			if (world instanceof World)
			{
				blockEntity = ((World)world).getChunk(pos).getBlockEntity(pos);
			}
		}
		return blockEntity != null && blockEntity.getType() == blockEntityType ? (S)blockEntity : null;
	}

	public static <S extends BlockEntity> DoubleBlockProperties.PropertySource<S> toPropertySource(BlockEntityType<S> blockEntityType, Function<BlockState, DoubleBlockProperties.Type> typeMapper, Function<BlockState, Direction> function, DirectionProperty directionProperty, BlockState state, IWorld world, BlockPos pos, BiPredicate<IWorld, BlockPos> fallbackTester) {
		S blockEntity = getBlockEntity(blockEntityType, world, pos);
		if (blockEntity == null) {
			return DoubleBlockProperties.PropertyRetriever::getFallback;
		} else if (fallbackTester.test(world, pos)) {
			return DoubleBlockProperties.PropertyRetriever::getFallback;
		} else {
			DoubleBlockProperties.Type type = (DoubleBlockProperties.Type)typeMapper.apply(state);
			boolean bl = type == DoubleBlockProperties.Type.SINGLE;
			boolean bl2 = type == DoubleBlockProperties.Type.FIRST;
			if (bl) {
				return new DoubleBlockProperties.PropertySource.Single<>(blockEntity);
			} else {
				BlockPos blockPos = pos.offset((Direction)function.apply(state));
				BlockState blockState = world.getBlockState(blockPos);
				if (blockState.getBlock() == state.getBlock()) {
					DoubleBlockProperties.Type type2 = (DoubleBlockProperties.Type)typeMapper.apply(blockState);
					if (type2 != DoubleBlockProperties.Type.SINGLE && type != type2 && blockState.get(directionProperty) == state.get(directionProperty)) {
						if (fallbackTester.test(world, blockPos)) {
							return DoubleBlockProperties.PropertyRetriever::getFallback;
						}

						S blockEntity2 = getBlockEntity(blockEntityType, world, blockPos);
						if (blockEntity2 != null) {
							S blockEntity3 = (S)(bl2 ? blockEntity : blockEntity2);
							S blockEntity4 = (S)(bl2 ? blockEntity2 : blockEntity);
							return new DoubleBlockProperties.PropertySource.Pair<>(blockEntity3, blockEntity4);
						}
					}
				}

				return new DoubleBlockProperties.PropertySource.Single<>(blockEntity);
			}
		}
	}

	public interface PropertyRetriever<S, T> {
		T getFromBoth(S first, S second);

		T getFrom(S single);

		T getFallback();
	}

	public interface PropertySource<S> {
		<T> T apply(DoubleBlockProperties.PropertyRetriever<? super S, T> retriever);

		public static final class Pair<S> implements DoubleBlockProperties.PropertySource<S> {
			private final S first;
			private final S second;

			public Pair(S first, S second) {
				this.first = first;
				this.second = second;
			}

			@Override
			public <T> T apply(DoubleBlockProperties.PropertyRetriever<? super S, T> propertyRetriever) {
				return propertyRetriever.getFromBoth(this.first, this.second);
			}
		}

		public static final class Single<S> implements DoubleBlockProperties.PropertySource<S> {
			private final S single;

			public Single(S single) {
				this.single = single;
			}

			@Override
			public <T> T apply(DoubleBlockProperties.PropertyRetriever<? super S, T> propertyRetriever) {
				return propertyRetriever.getFrom(this.single);
			}
		}
	}

	public static enum Type {
		SINGLE,
		FIRST,
		SECOND;
	}
}
