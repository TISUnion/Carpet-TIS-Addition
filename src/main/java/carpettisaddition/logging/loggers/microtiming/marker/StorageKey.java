package carpettisaddition.logging.loggers.microtiming.marker;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.Objects;

public class StorageKey
{
	private final DimensionType dimensionType;
	private final BlockPos blockPos;

	private StorageKey(DimensionType dimensionType, BlockPos blockPos)
	{
		this.dimensionType = dimensionType;
		this.blockPos = blockPos;
	}

	public StorageKey(World world, BlockPos blockPos)
	{
		this(world.getDimension().getType(), blockPos);
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		StorageKey that = (StorageKey) o;
		return Objects.equals(dimensionType, that.dimensionType) && Objects.equals(blockPos, that.blockPos);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(dimensionType, blockPos);
	}
}
