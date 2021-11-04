package carpettisaddition.logging.loggers.microtiming.marker;

import carpettisaddition.utils.DimensionWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;

public class StorageKey
{
	private final DimensionWrapper dimensionType;
	private final BlockPos blockPos;

	private StorageKey(DimensionWrapper dimensionType, BlockPos blockPos)
	{
		this.dimensionType = dimensionType;
		this.blockPos = blockPos;
	}

	public StorageKey(World world, BlockPos blockPos)
	{
		this(DimensionWrapper.of(world), blockPos);
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
