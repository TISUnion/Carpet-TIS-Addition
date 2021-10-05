package carpettisaddition.logging.loggers.microtiming.events;

import carpettisaddition.utils.TextUtil;
import net.minecraft.text.BaseText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public interface EventSource
{
	Object getSourceObject();

	BaseText getName();

	Identifier getId();

	static Optional<EventSource> fromObject(Object object)
	{
		if (object instanceof net.minecraft.block.Block)
		{
			return Optional.of(new Block((net.minecraft.block.Block)object));
		}
		else if (object instanceof net.minecraft.fluid.Fluid)
		{
			return Optional.of(new Fluid((net.minecraft.fluid.Fluid)object));
		}
		return Optional.empty();
	}

	class Block implements EventSource
	{
		private final net.minecraft.block.Block block;

		public Block(net.minecraft.block.Block block)
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
			return TextUtil.getBlockName(this.block);
		}

		@Override
		public Identifier getId()
		{
			return Registry.BLOCK.getId(this.block);
		}
	}

	class Fluid implements EventSource
	{
		private final net.minecraft.fluid.Fluid fluid;

		public Fluid(net.minecraft.fluid.Fluid fluid)
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
			return TextUtil.getFluidName(this.fluid);
		}

		@Override
		public Identifier getId()
		{
			return Registry.FLUID.getId(this.fluid);
		}
	}
}
