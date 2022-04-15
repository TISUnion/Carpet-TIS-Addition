package carpettisaddition.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.Objects;

/*
 * A wrapper class to deal with dimension type class differences between minecraft version:
 * - DimensionType in 1.15-
 * - RegistryKey<World> in 1.16+
 */
public class DimensionWrapper
{
	public static final DimensionWrapper OVERWORLD = of(World.OVERWORLD);
	public static final DimensionWrapper THE_NETHER = of(World.NETHER);
	public static final DimensionWrapper THE_END = of(World.END);

	private final RegistryKey<World> dimensionType;

	private DimensionWrapper(RegistryKey<World> dimensionType)
	{
		this.dimensionType = dimensionType;
	}

	public static DimensionWrapper of(RegistryKey<World> dimensionType)
	{
		return new DimensionWrapper(dimensionType);
	}

	public static DimensionWrapper of(World world)
	{
		return new DimensionWrapper(world.getRegistryKey());
	}

	public static DimensionWrapper of(Entity entity)
	{
		return of(entity.getEntityWorld());
	}

	/**
	 * Warning: mc version dependent
	 */
	public RegistryKey<World> getValue()
	{
		return this.dimensionType;
	}

	public Identifier getIdentifier()
	{
		return this.dimensionType.getValue();
	}

	public String getIdentifierString()
	{
		return this.getIdentifier().toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DimensionWrapper that = (DimensionWrapper) o;
		return Objects.equals(dimensionType, that.dimensionType);
	}

	@Override
	public int hashCode()
	{
		return this.dimensionType.hashCode();
	}

	@Deprecated
	@Override
	public String toString()
	{
		return this.getIdentifierString();
	}
}
