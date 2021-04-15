package carpettisaddition.logging.loggers.microtiming.utils;

import carpettisaddition.logging.loggers.microtiming.events.BaseEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class MicroTimingContext
{
	private World world;
	private BlockPos blockPos;
	private Supplier<BaseEvent> eventSupplier;
	private BiFunction<World, BlockPos, Optional<DyeColor>> woolGetter;
	private DyeColor color;
	private String blockName;

	public static MicroTimingContext create()
	{
		return new MicroTimingContext();
	}

	public World getWorld()
	{
		return this.world;
	}

	public BlockPos getBlockPos()
	{
		return this.blockPos;
	}

	public Supplier<BaseEvent> getEventSupplier()
	{
		return this.eventSupplier;
	}

	public BiFunction<World, BlockPos, Optional<DyeColor>> getWoolGetter()
	{
		return this.woolGetter;
	}

	public DyeColor getColor()
	{
		return this.color;
	}

	public String getBlockName()
	{
		return this.blockName;
	}

	public MicroTimingContext withWorld(World world)
	{
		this.world = world;
		return this;
	}

	public MicroTimingContext withBlockPos(BlockPos blockPos)
	{
		this.blockPos = blockPos.toImmutable();
		return this;
	}

	public MicroTimingContext withEventSupplier(Supplier<BaseEvent> eventSupplier)
	{
		this.eventSupplier = eventSupplier;
		return this;
	}

	public MicroTimingContext withEvent(BaseEvent event)
	{
		return this.withEventSupplier(() -> event);
	}

	public MicroTimingContext withWoolGetter(BiFunction<World, BlockPos, Optional<DyeColor>> woolGetter)
	{
		this.woolGetter = woolGetter;
		return this;
	}

	public MicroTimingContext withColor(DyeColor color)
	{
		this.color = color;
		return this;
	}

	public MicroTimingContext withBlockName(String blockName)
	{
		this.blockName = blockName;
		return this;
	}
}
