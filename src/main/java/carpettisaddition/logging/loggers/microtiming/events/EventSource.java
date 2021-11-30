package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.utils.IdentifierUtil;
import carpettisaddition.utils.Messenger;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.text.BaseText;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.Optional;

public abstract class EventSource
{
	public abstract Object getSourceObject();

	public abstract BaseText getName();

	public abstract Identifier getId();

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return Objects.equals(getSourceObject(), ((EventSource)o).getSourceObject());
	}

	@Override
	public int hashCode()
	{
		return this.getSourceObject().hashCode();
	}

	public static Optional<EventSource> fromObject(Object object)
	{
		if (object instanceof net.minecraft.block.Block)
		{
			return Optional.of(new BlockEventSource((Block)object));
		}
		else if (object instanceof net.minecraft.fluid.Fluid)
		{
			return Optional.of(new FluidEventSource((net.minecraft.fluid.Fluid)object));
		}
		return Optional.empty();
	}

	public static class BlockEventSource extends EventSource
	{
		private final Block block;

		public BlockEventSource(Block block)
		{
			this.block = block;
		}

		@Override
		public Object getSourceObject()
		{
			return this.block;
		}

		@Override
		public BaseText getName()
		{
			return Messenger.block(this.block);
		}

		@Override
		public Identifier getId()
		{
			return IdentifierUtil.id(this.block);
		}
	}

	public static class FluidEventSource extends EventSource
	{
		private final Fluid fluid;

		public FluidEventSource(Fluid fluid)
		{
			this.fluid = fluid;
		}

		@Override
		public Object getSourceObject()
		{
			return this.fluid;
		}

		@Override
		public BaseText getName()
		{
			return Messenger.fluid(this.fluid);
		}

		@Override
		public Identifier getId()
		{
			return IdentifierUtil.id(this.fluid);
		}
	}
}
